package ru.kochkaev.api.seasons.object;

import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.SeasonsAPI;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ConfigFileObject {

    private final ConfigContentObject content;

    private final String type;
    private final String modName;
    private final String filename;
    private final Path path;
    Integer version;

    protected ConfigFileObject(String modName, String filename, String type) {
        this(modName, filename, type, 1);
    }
    protected ConfigFileObject(String modName, String filename, String type, Integer version) {
        this.modName = modName;
        this.filename = filename;
        this.type = type;
        this.path = SeasonsAPI.getLoader().getConfigPath().resolve("Seasons/" + modName + (type.equals("lang") ? "/lang/" : "/") + filename + ".txt");
        this.content = new ConfigContentObject(this::generateFull);
        this.version = version;
    }
    protected ConfigFileObject(String modName, String filename, String type, String copyright) {
        this(modName, filename, type, copyright, 1);
    }
    protected ConfigFileObject(String modName, String filename, String type, String copyright, Integer version) {
        this.modName = modName;
        this.filename = filename;
        this.type = type;
        this.path = SeasonsAPI.getLoader().getConfigPath().resolve("Seasons/" + modName + (type.equals("lang") ? "/lang/" : "/") + filename + ".txt");
        this.content = new ConfigContentObject(this::generateFull, copyright);
        this.version = version;
    }

    /** Generate config file content and set of generated keys. */
    public abstract void generate(ConfigContentObject content);
    /** Generate config file content with additional information if it does not present (for example: config version). */
    public void generateFull(ConfigContentObject content) {
        generate(content);
        if (!content.containsKey("version"))
            content.addHeader("Technical Info").addValue("version", version, "Config version");
    }

    /** Calls after config file loaded */
    public Boolean update(ConfigContentObject content, Integer targetVersion, Integer currentVersion) { return  false; }

    public void reload() {
        try {
            autoParser(Files.readAllLines(path, StandardCharsets.UTF_8).stream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Text getText(String key) {
        var entry = content.get(key);
        return (entry instanceof ConfigTextValueObject val) ? val.getMinecraft() : null;
    }
    public String getString(String key) {
        Object value = getValue(key);
        return (value instanceof String) ? (String) value : null;
    }
    public Integer getInt(String key) {
        Object value = getValue(key);
        return (value instanceof Integer) ? (Integer) value : null;
    }
    public Long getLong(String key) {
        Object value = getValue(key);
        return (value instanceof Long) ? (Long) value : null;
    }
    public Float getFloat(String key) {
        Object value = getValue(key);
        return (value instanceof Float) ? (Float) value : null;
    }
    public Double getDouble(String key) {
        Object value = getValue(key);
        return (value instanceof Double) ? (Double) value : null;
    }
    public Boolean getBoolean(String key) {
        Object value = getValue(key);
        return (value instanceof Boolean) ? (Boolean) value : null;
    }
    public Object getValue(String key) {
        return content.get(key).getValue();
    }

    public void open(){
        try {
            content.reload();
            autoParser(Files.readAllLines(path, StandardCharsets.UTF_8).stream());
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
            File txt = path.toFile();
            if (!txt.exists()){
                txt.getParentFile().mkdirs();
                txt.createNewFile();
            }
            content.reload();
            boolean isLegacy0 = autoParser(Files.readAllLines(path, StandardCharsets.UTF_8).stream());
            boolean isLegacy1 = update(content, version, getInt("version"));
            if (isLegacy0 || isLegacy1) write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        content.clear();
    }

    public ConfigContentObject getContent() {
//        for (String key : keyCommentValueMap.getKeySet()) keyCommentValueMap.setSecond(key, map.get(key));
        return content;
    }
    public ConfigValueObject<?> getValueObject(String key) {
        return content.get(key);
    }
    public Queue<String> getKeysQueue() {
        return content.getQueue();
    }

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void write() {
        try {
//            content.reload();
            String generated = content.getGenerated();
            Writer writer = Files.newBufferedWriter(path);
            writer.write(generated);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public <T> void setValue(String key, T value) {
        content.get(key).setValue(value);
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
    public boolean autoParser(Stream<String> stream){
//        stream = Arrays.stream(stream.collect(Collectors.joining()).split("\n"));
//        String[] lines = stream.collect(Collectors.joining()).split("\n");
        AtomicBoolean hasVersion = new AtomicBoolean(false);
        Set<String> keySet = stream
                .filter(line -> !line.startsWith("#"))
                .filter(line -> !line.isEmpty())
                .map(line -> line.contains("#") ? line.substring(0, line.indexOf("#")) : line)
                .map((String line) -> {
                    String key = line.substring(0, line.indexOf(":"));
                    if (key.equals("version")) hasVersion.set(true);
                    parseAndAddValue(key, line.substring(line.indexOf("\"")+1, line.lastIndexOf("\"")));
                    return key;
                })
                .collect(Collectors.toSet());
        if (!hasVersion.get()) content.get("version").setValue(1);
        return content.keySet().stream().anyMatch(key -> !keySet.contains(key));
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

    public void parseAndAddValue(String key, String value) {
        ConfigValueObject<?> valueObject = content.get(key);
        if (valueObject!=null){
            valueObject.setValue(parseValue(valueObject, value));
        }
    }

    public static Object parseValue(ConfigValueObject<?> valueObject, String value) {
        return switch (valueObject.getValue()) {
            case String s -> value;
            case Boolean b -> Boolean.parseBoolean(value);
            case Integer i -> Integer.parseInt(value);
            case Long l -> Long.parseLong(value);
            case Float f -> Float.parseFloat(value);
            case Double d -> Double.parseDouble(value);
            case null, default -> null;
        };
    }

    public String getModName() {
        return modName;
    }
    public String getFilename(){
        return filename;
    }
    public String getType(){
        return type;
    }
}

//  This object was created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com