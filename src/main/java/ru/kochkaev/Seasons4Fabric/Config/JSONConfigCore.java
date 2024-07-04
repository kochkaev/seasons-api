package ru.kochkaev.Seasons4Fabric.Config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

class JSONConfigCoreTools {

    private final Path path;
    private JsonObject jsonObject;
    static Gson gson = new Gson();

    // If file isn't empty
    JSONConfigCoreTools(Path path, BufferedReader reader) {
        this.jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        this.path = path;
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // If file empty
    JSONConfigCoreTools(Path path, String defaults) {
        this.jsonObject = (JsonObject) JsonParser.parseString(defaults);
        this.path = path;
    }

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    public void writeJsonObject(JsonObject newJsonObject) {
        this.jsonObject = newJsonObject;
    }

    public void save() {
        try {
            Writer writer = Files.newBufferedWriter(path.toRealPath());
            writer.write(gson.toJson(jsonObject));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

public class JSONConfigCore {
    public static JSONConfigCoreTools openOrCreate(String filename, String defaults) {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(filename+".json");
        String pathStr = path.toString();
        try {
            File json = new File(pathStr);
            long jsonLen = json.length();
            if (!json.exists()){ json.createNewFile(); }
            BufferedReader reader = Files.newBufferedReader(path.toRealPath());
            JSONConfigCoreTools instance;
            if (jsonLen != 0L) instance = new JSONConfigCoreTools(path, reader);
            else instance = new JSONConfigCoreTools(path, defaults);
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