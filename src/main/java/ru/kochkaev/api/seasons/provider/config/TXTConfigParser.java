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
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TXTConfigParser {

    private static final Pattern fieldRegex = Pattern.compile("^\\s*(\\S+?)\\s*:\\s*(?<!\\\\)\"(.*?)(?<!\\\\)\"\\s*(?=#)");
    private static final Pattern listRegex = Pattern.compile("\\[(\\s*(?<!\\\\)\"(.*?)(?<!\\\\)\"\\s*,)*?]");
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
                        else if (field.getType() == Short.class) value = Short.parseShort(stringValue);
                        else if (field.getType() == Byte.class) value = Byte.parseByte(stringValue);
                        else if (field.getType() == Character.class) value = stringValue.charAt(0);
                        else if (field.getType() == Enum.class) value = Enum.valueOf((Class<Enum>) field.getType(), stringValue);
                        else if (field.getType() == Date.class) value = new Date(Long.parseLong(stringValue));
                        else if (field.getType() == List.class || field.getType() == Iterable.class || field.getType() == Object[].class)
                            value = fromTXTToList(field.getType(), stringValue);
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
            else if (field.getType() == List.class)
                stringValue = "["+(objectListToList(value).stream().map(TXTConfigParser::parseFieldValueToTXT).collect(Collectors.joining(",")))+"]";
            else stringValue = gson.toJson(value);
        }
        return stringValue;
    }
    private static String parseFieldValueToTXT(Object value) {
        return switch (value) {
            case String s -> s;
            case Integer i -> i.toString();
            case Boolean b -> b.toString();
            case Double v -> v.toString();
            case Long l -> l.toString();
            case Float v -> v.toString();
            case List<?> list ->
                    "[" + (objectListToList(list).stream().map(TXTConfigParser::parseFieldValueToTXT).map(it -> "\""+it+"\"").collect(Collectors.joining(","))) + "]";
            case Object[] array ->
                    "[" + (objectListToList(array).stream().map(TXTConfigParser::parseFieldValueToTXT).collect(Collectors.joining(","))) + "]";
            case Iterable<?> iterable ->
                    "[" + (objectListToList(iterable).stream().map(TXTConfigParser::parseFieldValueToTXT).collect(Collectors.joining(","))) + "]";
            case null, default -> gson.toJson(value);
        };
    }

    private static String escapeComment(String string) {
        return escapeComment(string, "\n# ");
    }
    private static String escapeComment(String string, String replacement) {
        return replacement + string.replace("\n", replacement);
    }

    private static <T> List<T> objectListToList(Object object) {
        final var list = new ArrayList<T>();
        if (object instanceof List) {
            list.addAll((List<T>) object);
        }
        else if (object instanceof Object[]) {
            list.addAll(Arrays.asList((T[]) object));
        }
        else if (object instanceof Iterable) {
            ((Iterable<T>)object).forEach(list::add);
        }
        else list.add((T) object);
        return list;
    }
    private static <T> List<T> fromTXTToList(Class<T> clazz, String string) {
        return new ArrayList<T>(
            listRegex.matcher(string).results().map(MatchResult::group).map(it -> {
                Object object;
                if (clazz == String.class) object = it;
                else if (clazz == Integer.class) object = Integer.parseInt(it);
                else if (clazz == Boolean.class) object = Boolean.parseBoolean(it);
                else if (clazz == Double.class) object = Double.parseDouble(it);
                else if (clazz == Long.class) object = Long.parseLong(it);
                else if (clazz == Float.class) object = Float.parseFloat(it);
                else if (clazz == Short.class) object = Short.parseShort(it);
                else if (clazz == Byte.class) object = Byte.parseByte(it);
                else if (clazz == Character.class) object = it.charAt(0);
                else if (clazz == Enum.class) object = Enum.valueOf((Class<Enum>) clazz, it);
                else if (clazz == Date.class) object = new Date(Long.parseLong(it));
                else if (clazz == List.class || clazz == Iterable.class || clazz == Object[].class)
                    object = fromTXTToList(clazz, it);
                else if (it.matches("\\s*\\{(.*?)}\\s")) object = gson.fromJson(it, clazz);
                else object = it;
                return (T) object;
            }).toList()
        );
    }

}
