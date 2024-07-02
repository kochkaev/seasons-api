package ru.kochkaev.Seasons4Fabric.Config;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TXTConfigCore {

    public static Map<String, String> openOrCreate(String filename, String defaults){
        Map<String, String> config;
        String pathStr = FabricLoader.getInstance().getConfigDir().resolve("").toString();
        try {
            File txt = new File(pathStr+"/"+filename+".txt");
            if (!txt.exists()){ txt.createNewFile(); }
            Reader reader = Files.newBufferedReader(Paths.get(pathStr+"/"+filename+".txt"));
            if (reader.toString().length() == 0){
                Writer writer = Files.newBufferedWriter(Paths.get(pathStr+"/"+filename+".txt"));
                writer.write(defaults);
                writer.close();
            }
            config = txtParser(reader.toString());
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
    public static Map<String, String> txtParser(String input){
        int countOfLineBreak = StringUtils.countMatches(input, "\n");
        Map<String, String> output = new HashMap<>();
        String temp, tempKey, tempValue;
        for (int i = 0; i<countOfLineBreak; i++){
            temp = input.substring(0, input.indexOf("\n"));
            input = input.substring(input.indexOf("\n")|input.length()-1);
            if (temp.length() > 0 && temp.charAt(0) != '#'){
                if (temp.equals("#")) { temp = temp.substring(0, temp.indexOf("#")-1); }
                tempKey = temp.substring(0, temp.indexOf(" ")-1);
                tempValue = temp.substring(temp.indexOf("\"")+1, temp.lastIndexOf("\"")-1);
                output.put(tempKey, tempValue);
            }
        }
        return output;
    }


    public static class GenerateDefaults {

        private String generated;

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

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com