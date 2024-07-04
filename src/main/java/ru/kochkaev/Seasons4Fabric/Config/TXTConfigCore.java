package ru.kochkaev.Seasons4Fabric.Config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TXTConfigCore {

    public static TXTMapObject openOrCreate(String filename, String defaults){
        TXTMapObject config;
        String pathStr = FabricLoader.getInstance().getConfigDir().resolve("").toString();
        try {
            File txt = new File(pathStr+"/"+filename+".txt");
            if (!txt.exists()){ txt.createNewFile(); }
            Scanner reader = new Scanner(txt, "UTF-8");
            if (txt.length() == 0){
                reader.close();
                Writer writer = Files.newBufferedWriter(Paths.get(pathStr+"/"+filename+".txt"));
                writer.write(defaults);
                writer.close();
                reader = new Scanner(txt, "UTF-8");
            }
            config = new TXTMapObject(pathStr+"/"+filename+".txt", reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }


    /** .txt file syntax:
     *  ----------------------------------
     *  # This is a values!
     *  key.of.value: "Example value!"
     *  key-of-value2: "Example value2!"
     *  keyOfValue3: "" # ;)
     *  ----------------------------------
     *  Don't insert a space before line!
     *  Empty line is available!
     */
    public static Map<String, String> txtParser(Scanner reader){
        Map<String, String> output = new HashMap<>();
        String temp, tempKey, tempValue;
        while (true) {
            if (reader.hasNextLine()) temp = reader.nextLine();
            else {
                temp = "";
                while (reader.hasNext()) {
                    temp += reader.next();
                }
            }
            if (temp.length() > 0 && temp.charAt(0) != '#') {
                if (temp.contains("#")) {
                    temp = temp.substring(0, temp.indexOf("#") - 1);
                }
                tempKey = temp.substring(0, temp.indexOf(" ") - 1);
                tempValue = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
                output.put(tempKey, tempValue);
            }
            if (!reader.hasNext()) break;
        }
        return output;
    }


    public static class GenerateDefaults {

        private String generated = "";

        public void addLine(String line) {
            generated += line + "\n";
        }
        public void addValue(String key, String value) {
            addLine(key + ": \"" + value + "\"");
        }
        public void addValueAndCommentDefault(String key, String value) {
            addLine(key + ": \"" + value + "\" # | default: \"" + value + "\"");
        }
        public void addComment(String comment) {
            addLine("# " + comment);
        }
        public void addVoid() { addLine(""); }
        public void addString(String string) { generated += string; }

        public String getGenerated() { return generated; }

    }

}

class TXTMapObject {

    private String path;
    private Map<String, String> map;

    TXTMapObject (String path, Map<String, String> map) {
        this.path = path;
        this.map = map;
    }

    TXTMapObject (String path, Scanner reader) {
        this.path = path;
        this.map = TXTConfigCore.txtParser(reader);
    }

    public void reload() {
        try {
            File txt = new File(path);
            Scanner reader = new Scanner(txt, "UTF-8");
            this.map = TXTConfigCore.txtParser(reader);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) { return map.get(key); }
}

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com