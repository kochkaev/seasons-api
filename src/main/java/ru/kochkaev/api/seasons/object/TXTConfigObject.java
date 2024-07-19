package ru.kochkaev.api.seasons.object;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TXTConfigObject {

    private String path;
    private Map<String, String> map;
    private String type;

    private String filename;
    private String subType;

    protected TXTConfigObject(String modName, String filename, String type) {
        this.filename = modName + (type.equals("lang") ? "/lang/" : "/") + filename;
        this.subType = filename;
        this.type = type;
    }

//    TXTConfigObject (String path, Map<String, String> map) {
//        this.path = path;
//        this.map = map;
//    }

    TXTConfigObject (String path, Scanner reader) {
        this.path = path;
        this.map = txtParser(reader);
    }

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


    public static TXTConfigObject openOrCreate(String filename, String defaults){
        TXTConfigObject config;
        String pathStr = FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve("").toString();
        try {
            File txt = new File(pathStr+"/"+filename+".txt");
            if (!txt.exists()){
                boolean uselessly = txt.getParentFile().mkdirs();
                boolean uselessly1 = txt.createNewFile();
            }
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            if (txt.length() == 0){
                reader.close();
                Writer writer = Files.newBufferedWriter(Paths.get(pathStr+"/"+filename+".txt"));
                writer.write(defaults);
                writer.close();
                reader = new Scanner(txt, StandardCharsets.UTF_8);
            }
            config = new TXTConfigObject(pathStr+"/"+filename+".txt", reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
    public TXTConfigObject openOrCreate(){
        return openOrCreate("Seasons/" + filename, getGenerated());
    }

    public static TXTConfigObject open(String filename){
        TXTConfigObject config;
        String pathStr = FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve(filename+".txt").toString();
        try {
            File txt = new File(pathStr);
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            config = new TXTConfigObject(pathStr, reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
    public static TXTConfigObject openOrDefault(String filename, String defaultFilename){
        TXTConfigObject config;
        String pathStr = FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve(filename+".txt").toString();
        try {
            File txt = new File(pathStr);
            if (!txt.exists()) txt = new File(FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve(defaultFilename+".txt").toString());
            Scanner reader = new Scanner(txt, StandardCharsets.UTF_8);
            config = new TXTConfigObject(pathStr, reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
    public TXTConfigObject open() {
        return open("Seasons/" + filename);
    }

    public static void createIfDoNotExists(String filename, String defaults){
        //TXTMapObject config;
        String pathStr = FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve(filename+".txt").toString();
        try {
            File txt = new File(pathStr);
            if (!txt.exists()){
                boolean uselessly = txt.getParentFile().mkdirs();
                boolean uselessly1 = txt.createNewFile();
            }
            if (txt.length() == 0){
                Writer writer = Files.newBufferedWriter(Paths.get(pathStr));
                writer.write(defaults);
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void createIfDoNotExists(){
        createIfDoNotExists("Seasons/" + filename, getGenerated());
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


//    public static class GenerateDefaults {

    private String generated = "";

    protected void addLine(String line) {
        generated += line + "\n";
    }
    protected void addValue(String key, String value) {
        addLine(key + ": \"" + value + "\"");
    }
    protected void addValueAndCommentDefault(String key, String value) {
        addLine(key + ": \"" + value + "\" # | default: \"" + value + "\"");
    }
    protected void addComment(String comment) {
        addLine("# " + comment);
    }
    protected void addVoid() { addLine(""); }
    protected void addString(String string) { generated += string; }

    protected String getGenerated() { return generated; }

//    }

    public String getType() { return type; }
    public String getSubType() { return subType; }

}

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com