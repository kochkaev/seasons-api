package ru.kochkaev.Seasons4Fabric.Config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JSONConfigCore {

    private Writer writer;
    private Map<String, String> map;
    // This Map for primitive Json!

    Gson gson = new Gson();

    JSONConfigCore(Writer writer) {
        this.writer = writer;
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        String writerStr = writer.toString().length()>0 ? writer.toString() : "{}";
        this.map = new Gson().fromJson(writerStr, mapType);
    }

    public Map<String, String> getMap() { return this.map; }

    public void writeMap(Map<String, String> newMap) { this.map = newMap; }

    public void close() {
        try {
            writer.write(gson.toJson(map));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONConfigCore openOrCreate(String filename) {
        String pathStr = FabricLoader.getInstance().getConfigDir().resolve("").toString();
        try {
            File json = new File(pathStr+"/"+filename+".json");
            if (!json.exists()){ json.createNewFile(); }
            Writer writer = Files.newBufferedWriter(Paths.get(pathStr+"/"+filename+".json"));
            JSONConfigCore instance = new JSONConfigCore(writer);
            if (writer.toString().length() == 0){
                instance.getMap().put("season", "WINTER");
                instance.getMap().put("weather", "NIGHT");
            }
            return instance;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com