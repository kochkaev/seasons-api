package ru.kochkaev.api.seasons.object;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONConfigObject {

    private final Path path;
    private JsonObject jsonObject;
    static Gson gson = new Gson();

    public static JSONConfigObject openOrCreate(Path path, String defaults) {
        String pathStr = path.toString();
        try {
            File json = new File(pathStr);
            long jsonLen = json.length();
            if (!json.exists()){
                boolean uselessly = json.getParentFile().mkdirs();
                boolean uselessly1 = json.createNewFile();
            }
            BufferedReader reader = Files.newBufferedReader(path.toRealPath());
            JSONConfigObject instance;
            if (jsonLen != 0L) instance = new JSONConfigObject(path, reader);
            else instance = new JSONConfigObject(path, defaults);
            return instance;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static JSONConfigObject openOrCreate(String filename, String defaults) {
        Path path = FabricLoader.getInstance().getConfigDir().toAbsolutePath().resolve(filename+".json");
        return openOrCreate(path, defaults);
    }


    // If file isn't empty
    JSONConfigObject(Path path, BufferedReader reader) {
        this.jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        this.path = path;
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // If file empty
    JSONConfigObject(Path path, String defaults) {
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

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com