package ru.kochkaev.api.seasons.provider.config;

import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;
import ru.kochkaev.api.seasons.provider.config.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TXTConfigParser {

    private static final Pattern fieldRegex = Pattern.compile("^\\s*(\\S+?)\\s*:\\s*(?<!\\\\)\"(.*?)(?<!\\\\)\"\\s*(?=#)");
    private static final Gson gson = new Gson();

    public static <T> T reload(Class<T> clazz, Path path) {
        T object = null;
        try {
            File txt = path.toFile();
            if (!txt.exists()){
                txt.getParentFile().mkdirs();
                txt.createNewFile();
            }
            final var fields = clazz.getDeclaredFields();
            final var content = txtParser(Files.readAllLines(path, StandardCharsets.UTF_8).stream());
            var isLegacy = false;
            final var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            object = constructor.newInstance();
            for (var field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(TXTConfigIgnore.class) || field.isAnnotationPresent(TXTConfigSecondFile.class)) continue;
                final var key = (field.isAnnotationPresent(TXTConfigCustomKey.class)) ? field.getAnnotation(TXTConfigCustomKey.class).value() : field.getName();
                Object value;
                final var stringValue = content.get(key);
                if (content.containsKey(key)) {
                    if (field.isAnnotationPresent(TXTConfigCustomFromTXTParser.class)) {
                        final var parser = clazz.getMethod(field.getAnnotation(TXTConfigCustomFromTXTParser.class).value());
                        value = parser.invoke(stringValue);
                    } else {
                        if (field.getType() == String.class) value = stringValue;
                        else if (field.getType() == Integer.class) value = Integer.parseInt(stringValue);
                        else if (field.getType() == Boolean.class) value = Boolean.parseBoolean(stringValue);
                        else if (field.getType() == Double.class) value = Double.parseDouble(stringValue);
                        else if (field.getType() == Long.class) value = Long.parseLong(stringValue);
                        else if (field.getType() == Float.class) value = Float.parseFloat(stringValue);
                        else if (stringValue.matches("\\s*\\{(.*?)}\\s")) value = gson.fromJson(stringValue, field.getType());
                        else value = stringValue;
                    }
                } else {
                    isLegacy = true;
                    value = stringValue;
                }
                field.set(object, value);
            }
            if (isLegacy) update(object, path);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return object;
    }
    public static Map<String, String> txtParser(Stream<String> stream){
        final var lines = stream.collect(Collectors.joining()).split("\n");
        final var map = new HashMap<String, String>();
        for (String line : lines) {
            final var matcher = fieldRegex.matcher(line);
            if (matcher.find()) {
                final var resultsCount = matcher.results().count();
                if (resultsCount>=2) map.put(matcher.group(1), matcher.group(2));
            }
        }
        return map;
    }

    public static void update(Object object, Path path) {update(object, path, null);}
    public static void update(Object object, Path path, @Nullable Map<String, String> defaults) {
        final var clazz = object.getClass();
        final var copyright = (clazz.isAnnotationPresent(TXTConfigCopyright.class)) ? clazz.getAnnotation(TXTConfigCopyright.class).value() : null;
        update(object, path, defaults, copyright);
    }
    public static void update(Object object, Path path, @Nullable Map<String, String> defaults, @Nullable String copyright){
        final var clazz = object.getClass();
        StringBuilder generated = new StringBuilder();
        if (copyright != null) generated.append(escapeComment(copyright)).append("\n\n\n");
        try {
            if (defaults == null) {
                final var blankInstance = clazz.getDeclaredConstructor().newInstance();
                defaults = new HashMap<>();
                for (var field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    final var key = (field.isAnnotationPresent(TXTConfigCustomKey.class)) ? field.getAnnotation(TXTConfigCustomKey.class).value() : field.getName();
                    final var stringValue = parseFieldValueToTXT(clazz, field, field.get(blankInstance));
                    defaults.put(key, stringValue);
                }
            }
            for (var field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(TXTConfigIgnore.class) || field.isAnnotationPresent(TXTConfigSecondFile.class)) continue;
                final var key = (field.isAnnotationPresent(TXTConfigCustomKey.class)) ? field.getAnnotation(TXTConfigCustomKey.class).value() : field.getName();
                final var stringValue = parseFieldValueToTXT(clazz, field, field.get(object));
                final var builder = new StringBuilder();
                if (field.isAnnotationPresent(TXTConfigHeader.class)) builder.append("\n\n# * ")
                        .append(escapeComment(field.getAnnotation(TXTConfigHeader.class).value(), "\n# * "));
                for (var annotation : field.getAnnotationsByType(TXTConfigDescription.class))
                    builder.append("\n# ").append(escapeComment(annotation.value()));
                if (field.isAnnotationPresent(TXTConfigAutoAddMeta.class)) {
                    final var metaConf = field.getAnnotation(TXTConfigAutoAddMeta.class);
                    builder.append("\n#");
                    if (metaConf.addType()) builder.append(" type: \"").append(field.getType().getSimpleName()).append("\"");
                    if (metaConf.addDefault()) builder.append(" default: \"").append(defaults.get(key)).append("\"");
                }
                builder.append("\n").append(key).append(": ").append(stringValue);
                if (field.isAnnotationPresent(TXTConfigInlineComment.class)) builder.append(" # ").append(field.getAnnotation(TXTConfigInlineComment.class).value());
                generated.append(builder);
            }
            Writer writer = Files.newBufferedWriter(path);
            writer.write(generated.toString());
            writer.close();
        } catch (IOException | IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static String parseFieldValueToTXT(Class<?> clazz, Field field, Object value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        var stringValue = "";
        if (field.isAnnotationPresent(TXTConfigCustomToTXTParser.class)) {
            final var parser = clazz.getMethod(field.getAnnotation(TXTConfigCustomToTXTParser.class).value());
            stringValue = parser.invoke(value).toString();
        } else {
            if (field.getType() == String.class) stringValue = value.toString();
            else if (field.getType() == Integer.class) stringValue = value.toString();
            else if (field.getType() == Boolean.class) stringValue = value.toString();
            else if (field.getType() == Double.class) stringValue = value.toString();
            else if (field.getType() == Long.class) stringValue = value.toString();
            else if (field.getType() == Float.class) stringValue = value.toString();
            else stringValue = gson.toJson(value);
        }
        return stringValue;
    }

    private static String escapeComment(String string) {
        return escapeComment(string, "\n# ");
    }
    private static String escapeComment(String string, String replacement) {
        return string.replace("\n", replacement);
    }

}
