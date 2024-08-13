package ru.kochkaev.api.seasons.object;

import net.fabricmc.loader.api.FabricLoader;
import ru.kochkaev.api.seasons.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class TXTConfigObject {

    private final String path;
    private Map<String, String> map = new HashMap<>();
    /** If true - {@link #generate} will generate content of config file, or else - generate only set of config keys. */
    private boolean generateMode = false;
    private final String type;

    private final String modName;
    private final String subType;

    private final Set<String> generatedKeySet = new HashSet<>();
    private String generated = "";

    protected TXTConfigObject(String modName, String filename, String type) {
        this.modName = modName;
//        this.filename = modName + (type.equals("lang") ? "/lang/" : "/") + filename;
        this.subType = filename;
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
            this.map = txtParser(reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        return map.get(key);
    }
    public int getInt(String key) {
        return Integer.parseInt(map.get(key));
    }
    public long getLong(String key) {
        return Long.parseLong(map.get(key));
    }
    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(map.get(key));
    }

    public void open(){
        try {
            File txt = new File(path);
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            map = txtParser(reader);
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
            map = txtParser(reader);
            reader.close();
            generate();
            if (!map.keySet().equals(generatedKeySet)) {
                generateMode = true;
                generate();
                generateMode = false;
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                writer.write(generated);
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        map.clear();
        generatedKeySet.clear();
        generated = "";
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
        if (generateMode) generated += line + "\n";
    }
    protected void addValue(String key, String value) {
        if (!generateMode) generatedKeySet.add(key);
        else {
            addLine(key + ": \"" + (map.getOrDefault(key, value)) + "\"");
            map.put(key, value);
        }
    }
    protected void addValueAndCommentDefault(String key, String value) {
        if (!generateMode) generatedKeySet.add(key);
        else {
            String valueModified = map.getOrDefault(key, value);
            addLine(key + ": \"" + valueModified + "\" # | default: \"" + value + "\"");
            map.put(key, value);
        }
    }
    protected void addComment(String comment) {
        addLine("# " + comment);
    }
    protected void addVoid() { addLine(""); }
    protected void addString(String string) { if (generateMode) generated += string; }

    protected String getGenerated() { return generated; }

//    }

    public String getType() { return type; }
    public String getSubType() { return subType; }
    public String getModName() { return modName; }

}

//  This object was created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com