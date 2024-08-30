package ru.kochkaev.api.seasons.object;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;
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
    private Map<String, String> valuesMap = new HashMap<>();
    private Map<String, Object> typesMap = new HashMap<>();
    /** If true - {@link #generate} will generate content of config file, or else - generate only set of config keys. */
    private String generateMode = "structure";
    private final String type;

    private final String modName;
    private final String filename;

    private final Queue<String> generatedKeySet = new PriorityQueue<>();
    private String generated = "";
    private final Map1Key3Values<String, String, String, Object> keyHeaderCommentValueMap = new Map1Key3Values.TreeMap1Key3Values<>();
    private String tempCommentForValue = "";
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
            this.valuesMap = txtParser(reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        return valuesMap.get(key);
    }
    public int getInt(String key) {
        return Integer.parseInt(valuesMap.get(key));
    }
    public long getLong(String key) {
        return Long.parseLong(valuesMap.get(key));
    }
    public float getFloat(String key) {
        return Float.parseFloat(valuesMap.get(key));
    }
    public double getDouble(String key) {
        return Double.parseDouble(valuesMap.get(key));
    }
    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(valuesMap.get(key));
    }
    public Object getValue(String key) {
//        return typesMap.get(key).getClass().cast(valuesMap.get(key));
        Object type = typesMap.get(key);
        Object value = valuesMap.get(key);
        if (type instanceof String) {
            return value;
        }
        else if (type instanceof Integer) {
            value = Integer.parseInt(StringUtils.join(value));
        }
        else if (type instanceof Long) {
            value = Long.parseLong(StringUtils.join(value));
        }
        else if (type instanceof Float) {
            value = Float.parseFloat(StringUtils.join(value));
        }
        else if (type instanceof Double) {
            value = Double.parseDouble(StringUtils.join(value));
        }
        else if (type instanceof Boolean) {
            value = Boolean.parseBoolean(StringUtils.join(value));
        }
        return value;
    }

    public void open(){
        try {
            File txt = new File(path);
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            valuesMap = txtParser(reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This universal method will generate a config file content, create file if it does not exist, open file and update content if it is legacy.
     */
    public void generateCreateIfDoNotExistsOpenAndUpdateIfLegacy() {
        try {
            File txt = new File(path);
            if (!txt.exists()){
                boolean uselessly = txt.getParentFile().mkdirs();
                boolean uselessly1 = txt.createNewFile();
            }
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            valuesMap = txtParser(reader);
            reader.close();
            generate();
            if (!valuesMap.keySet().equals(generatedKeySet)) {
                generateMode = "content";
                generate();
                generateMode = "structure";
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                writer.write(generated);
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        valuesMap.clear();
        generatedKeySet.clear();
        generated = "";
    }

    /** To Cloth Config */
    public Map1Key3Values<String, String, String, Object> getKeyHeaderCommentAndDefaultMap() {
        generateMode = "gui";
        generate();
        generateMode = "structure";
//        for (String key : keyCommentValueMap.getKeySet()) keyCommentValueMap.setSecond(key, map.get(key));
        Map1Key3Values<String, String, String, Object> output = keyHeaderCommentValueMap.copy();
        keyHeaderCommentValueMap.clear();
        tempCommentForValue = "";
        tempHeaderForValue = "";
        return output;
    }

    public void write(String key, String value) {
        valuesMap.put(key, value);
        try {
            generateMode = "content";
            generate();
            generateMode = "structure";
            Writer writer = Files.newBufferedWriter(Paths.get(path));
            writer.write(generated);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


//    public static class GenerateDefaults {

    protected void addLine(String line) {
        if (generateMode.equals("content")) generated += line + "\n";
    }
    protected void addValue(String key, String value) {
        switch (generateMode) {
            case "structure" -> {
                generatedKeySet.add(key);
                typesMap.put(key, String.class);
            }
            case "content" -> {
                addLine(key + ": \"" + (valuesMap.getOrDefault(key, value)) + "\"");
                valuesMap.put(key, value);
            }
            case "gui" -> {
                keyHeaderCommentValueMap.put(key, tempHeaderForValue, tempCommentForValue, value);
                tempCommentForValue = "";
            }
        }
    }
    protected void addValueAndCommentDefault(String key, String value) {
        switch (generateMode) {
            case "structure" -> {
                generatedKeySet.add(key);
                typesMap.put(key, String.class);
            }
            case "content" -> {
                String valueModified = valuesMap.getOrDefault(key, value);
                addLine(key + ": \"" + valueModified + "\" # | default: \"" + value + "\"");
                valuesMap.put(key, value);
            }
            case "gui" -> {
                keyHeaderCommentValueMap.put(key, tempHeaderForValue, tempCommentForValue, value);
                tempCommentForValue = "";
            }
        }
    }
    protected void addTypedValue(String key, Object value) {
        switch (generateMode) {
            case "structure" -> {
                generatedKeySet.add(key);
                typesMap.put(key, value.getClass());
            }
            case "content" -> {
                String valueModified = valuesMap.getOrDefault(key, StringUtils.join(value));
                addLine(key + ": \"" + valueModified + "\" # | type: \"" + value.getClass().getName() + "\"");
                valuesMap.put(key, StringUtils.join(value));
            }
            case "gui" -> {
                keyHeaderCommentValueMap.put(key, tempHeaderForValue, tempCommentForValue, value);
                tempCommentForValue = "";
            }
        }
    }
    protected void addTypedValueAndCommentDefault(String key, Object value) {
        switch (generateMode) {
            case "structure" -> {
                generatedKeySet.add(key);
                typesMap.put(key, value.getClass());
            }
            case "content" -> {
                String valueModified = valuesMap.getOrDefault(key, StringUtils.join(value));
                addLine(key + ": \"" + valueModified + "\" # | type: \"" + value.getClass().getName() + "\" | default: \"" + value + "\"");
                valuesMap.put(key, StringUtils.join(value));
            }
            case "gui" -> {
                keyHeaderCommentValueMap.put(key, tempHeaderForValue, tempCommentForValue, value);
                tempCommentForValue = "";
            }
        }
    }
    protected void addComment(String comment) {
        if (generateMode.equals("gui")) tempCommentForValue += "\n" + comment;
        addLine("# " + comment);
    }
    protected void addHeader(String header) {
        if (generateMode.equals("gui")) tempHeaderForValue = header;
        addLine("# * " + header);
    }
    protected void addVoid() { if (generateMode.equals("gui")) tempCommentForValue = ""; else addLine(""); }
    protected void addString(String string) { if (generateMode.equals("content")) generated += string; }

    protected String getGenerated() { return generated; }

//    }

    public String getType() { return type; }
    public String getFilename() { return filename; }
    public String getModName() { return modName; }

}

//  This object was created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com