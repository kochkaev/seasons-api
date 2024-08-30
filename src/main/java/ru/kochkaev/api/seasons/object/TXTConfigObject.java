package ru.kochkaev.api.seasons.object;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.service.Task;
import ru.kochkaev.api.seasons.util.map.Map1Key3Values;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TXTConfigObject {

    private final String path;
    private final Map<String, ConfigValueObject<?>> typedValuesMap = new TreeMap<>();
    /** If true - {@link #generate} will generate content of config file, or else - generate only set of config keys. */
    private String generateMode = "structure";
    private final String type;

    private final String modName;
    private final String filename;

    private String generated = "";
    private String tempCommentForValue = "—————";
    private String tempHeaderForValue = "";

    protected TXTConfigObject(String modName, String filename, String type) {
        this.modName = modName;
//        this.filename = modName + (type.equals("lang") ? "/lang/" : "/") + filename;
        this.filename = filename;
        this.type = type;
        this.path = FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve("Seasons/" + modName + (type.equals("lang") ? "/lang/" : "/") + filename + ".txt").toString();
    }

//    TXTConfigObject (String path, Map<String, String> map) {
//        this.path = path;
//        this.map = map;
//    }

//    private void readTXT (String path, Scanner reader) {
//        this.path = path;
//        this.map = txtParser(reader);
//    }

    /** Generate config file content and set of generated keys. */
    public abstract void generate();

    public void reload() {
        try {
            File txt = new File(path);
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            autoParser(reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        return getValue(key);
//        return valuesMap.get(key);
    }
    public int getInt(String key) {
        return getValue(key);
//        return Integer.parseInt(valuesMap.get(key));
    }
    public long getLong(String key) {
        return getValue(key);
//        return Long.parseLong(valuesMap.get(key));
    }
    public float getFloat(String key) {
        return getValue(key);
//        return Float.parseFloat(valuesMap.get(key));
    }
    public double getDouble(String key) {
        return getValue(key);
//        return Double.parseDouble(valuesMap.get(key));
    }
    public Boolean getBoolean(String key) {
        return getValue(key);
//        return Boolean.parseBoolean(valuesMap.get(key));
    }
    public <T> T getValue(String key) {
//        return typesMap.get(key).getClass().cast(valuesMap.get(key));
//        return (T) typesMap.get(key);
        //noinspection unchecked
        return (T) typedValuesMap.get(key).getValue();
    }

    public void open(){
        try {
            File txt = new File(path);
            generate();
            tempCommentForValue = "—————";
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            autoParser(reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This universal method will generate a config file content, create file if it does not exist, open file and update content if it is legacy.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void generateCreateIfDoNotExistsOpenAndUpdateIfLegacy() {
        try {
            File txt = new File(path);
            if (!txt.exists()){
                txt.getParentFile().mkdirs();
                txt.createNewFile();
            }
            generate();
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            Set<String> parsed = autoParser(reader);
            reader.close();
            tempCommentForValue = "—————";
            if (!parsed.equals(typedValuesMap.keySet())) {
                generateMode = "content";
                generated = "";
                generate();
                generateMode = "structure";
//                txt.delete();
//                txt.createNewFile();
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                writer.write(generated);
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        typedValuesMap.clear();
        generated = "";
    }

    /** To Cloth Config */
    public Map<String, ConfigValueObject<?>> getTypedValuesMap() {
//        for (String key : keyCommentValueMap.getKeySet()) keyCommentValueMap.setSecond(key, map.get(key));
        return typedValuesMap;
    }

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void write() {
        try {
            generateMode = "content";
            generated = "";
            generate();
            generateMode = "structure";
//            File txt = new File(path);
//            txt.delete();
//            txt.createNewFile();
            Writer writer = Files.newBufferedWriter(Paths.get(path));
            writer.write(generated);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public <T> void setValue(String key, T value) {
//        @SuppressWarnings("unchecked")
//        ConfigValueObject<?> confValue = typedValuesMap.get(key);
        typedValuesMap.get(key).setValue(value);
//        if (prev instanceof T) typedValuesMap.get(key).setValue(value);
//        if (SeasonsAPI.getServer()!=null && !Task.getTasks().getKeySet().contains("Configs-Rewrite"))
//            Task.addTask("Configs-Rewrite", args -> {
//                write();
//                Task.removeTask("Configs-Rewrite");
//                return args;
//            }, new ArrayList<>());
        write();
    }


    /** .txt file syntax: <br>
     *  ---------------------------------- <br>
     *  # This is a values! <br>
     *  key.of.value: "Example value!" <br>
     *  key-of-value2: "Example value2!" <br>
     *  keyOfValue3: "" # ;) <br>
     *  ---------------------------------- <br>
     *  Don't insert a space before line! <br>
     *  Empty line is available!
     */
    public static Map<String, String> txtParser(Scanner reader){
        Map<String, String> output = new HashMap<>();
        StringBuilder temp;
        String tempKey;
        String tempValue;
        do {
            if (reader.hasNextLine()) temp = new StringBuilder(reader.nextLine());
            else {
                temp = new StringBuilder();
                while (reader.hasNext()) {
                    temp.append(reader.next());
                }
            }
            if ((!temp.isEmpty()) && temp.charAt(0) != '#') {
                if (temp.toString().contains("#")) {
                    temp = new StringBuilder(temp.substring(0, temp.indexOf("#") - 1));
                }
                tempKey = temp.substring(0, temp.indexOf(" ") - 1);
                tempValue = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
                output.put(tempKey, tempValue);
            }
        } while (reader.hasNext());
        return output;
    }
//    public static Map<String, String> txtParser(Scanner reader){
//        final Spliterator<String> splt = Spliterators.spliterator(reader, Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
//        return txtParser(StreamSupport.stream(splt, false));
//    }
    public static Map<String, String> txtParser(String content){
        return txtParser(Arrays.stream(content.split("\n")));
    }
    public static Map<String, String> txtParser(Stream<String> stream){
        return stream
                .filter(line -> !line.startsWith("#"))
                .filter(line -> !line.isEmpty())
                .map(line -> line.contains("#") ? line.substring(0, line.indexOf("#")) : line)
                .collect(Collectors.toMap(
                        (String line) -> line.substring(0, line.indexOf(":")),
                        (String line) -> line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""))
                ));
    }

    public Set<String> autoParser(Scanner reader){
        Set<String> output = new HashSet<>();
        StringBuilder temp;
        String tempKey;
        String tempValue;
        Object parsedValue;
        do {
            if (reader.hasNextLine()) temp = new StringBuilder(reader.nextLine());
            else {
                temp = new StringBuilder();
                while (reader.hasNext()) {
                    temp.append(reader.next());
                }
            }
            if ((!temp.isEmpty()) && temp.charAt(0) != '#') {
                if (temp.toString().contains("#")) {
                    temp = new StringBuilder(temp.substring(0, temp.indexOf("#") - 1));
                }
                tempKey = temp.substring(0, temp.indexOf(" ") - 1);
                tempValue = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
                parsedValue = switch (typedValuesMap.get(tempKey).getValue()) {
                    case String s -> tempValue;
                    case Boolean b -> Boolean.parseBoolean(tempValue);
                    case Integer i -> Integer.parseInt(tempValue);
                    case Long l -> Long.parseLong(tempValue);
                    case Float f -> Float.parseFloat(tempValue);
                    case Double d -> Double.parseDouble(tempValue);
                    case null, default -> null;
                };
                typedValuesMap.get(tempKey).setValue(parsedValue);
                output.add(tempKey);
            }
        } while (reader.hasNext());
        return output;
    }


//    public static class GenerateDefaults {

    protected void addLine(String line) {
        if (generateMode.equals("content")) generated += line + "\n";
    }
    protected void addValue(String key, String value) {
        switch (generateMode) {
            case "structure" -> {
                typedValuesMap.put(key, new ConfigValueObject<>(value, value, tempHeaderForValue, tempCommentForValue));
                tempCommentForValue = "—————";
            }
            case "content" -> {
                addLine(key + ": \"" + (typedValuesMap.get(key).getValue()) + "\"");
            }
        }
    }
    protected void addValueAndCommentDefault(String key, String value) {
        switch (generateMode) {
            case "structure" -> {
                typedValuesMap.put(key, new ConfigValueObject<>(value, value, tempHeaderForValue, tempCommentForValue));
                tempCommentForValue = "—————";
            }
            case "content" -> addLine(key + ": \"" + typedValuesMap.get(key).getValue() + "\" # | default: \"" + value + "\"");
        }
    }
    protected void addTypedValue(String key, Object value) {
        switch (generateMode) {
            case "structure" -> {
                typedValuesMap.put(key, new ConfigValueObject<>(value, value, tempHeaderForValue, tempCommentForValue));
                tempCommentForValue = "—————";
            }
            case "content" -> addLine(key + ": \"" + typedValuesMap.get(key).getValue() + "\" # | type: \"" + value.getClass().getSimpleName() + "\"");
        }
    }
    protected void addTypedValueAndCommentDefault(String key, Object value) {
        switch (generateMode) {
            case "structure" -> {
                typedValuesMap.put(key, new ConfigValueObject<>(value, value, tempHeaderForValue, tempCommentForValue));
                tempCommentForValue = "—————";
            }
            case "content" -> addLine(key + ": \"" + typedValuesMap.get(key).getValue() + "\" # | type: \"" + value.getClass().getSimpleName() + "\" | default: \"" + value + "\"");
        }
    }
    protected void addComment(String comment) {
        switch (generateMode) {
            case "structure" -> tempCommentForValue += "\n" + comment;
            case "content" -> addLine("# " + comment);
        }
    }
    protected void addHeader(String header) {
        switch (generateMode) {
            case "structure" -> tempHeaderForValue = header;
            case "content" -> addLine("# * " + header);
        }
    }
    protected void addVoid() {
        switch (generateMode) {
            case "structure" -> tempCommentForValue = "—————";
            case "content" -> addLine("");
        }
    }
    protected void addString(String string) { if (generateMode.equals("content")) generated += string; }

    protected String getGenerated() { return generated; }

//    }

    public String getType() { return type; }
    public String getFilename() { return filename; }
    public String getModName() { return modName; }

    public static class ConfigValueObject<T> {
        private T value;
        private final T defaultValue;
        private String header;
        private String description;

        public ConfigValueObject(T value, T defaultValue, String header, String description) {
            this.value = value;
            this.defaultValue = defaultValue;
            this.header = header;
            this.description = description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public void setHeader(String header) {
            this.header = header;
        }
//        public void setValue(T value) {
//            this.value = value;
//        }
        public void setValue(Object value) {
            //noinspection unchecked
            this.value = (T) value;
        }

        public String getDescription() {
            return description;
        }
        public String getHeader() {
            return header;
        }
        public T getValue() {
            return value;
        }
        public T getDefaultValue() {
            return defaultValue;
        }
    }

}

//  This object was created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com