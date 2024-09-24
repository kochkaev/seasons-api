package ru.kochkaev.api.seasons.provider;

import com.google.gson.JsonObject;
import net.minecraft.util.WorldSavePath;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.object.ConfigFileObject;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.object.ConfigValueObject;
import ru.kochkaev.api.seasons.object.JSONConfigObject;

import java.util.*;

public final class Config {

    private static JsonObject current = new JsonObject();
    private static final Map<String, ConfigValueObject<?>> currentMap = new HashMap<>();
    private static JSONConfigObject jsonCore;
    private static final Map<String, ConfigObject> mods = new HashMap<>();
    private static final List<String> listOfLangs =  new ArrayList<>();
    private static String currentLang = "en_US";

    public static void initConfigObjects(){
        Config.getModConfig("API").getConfig().getString("conf.lang");
        for (ConfigObject mod : mods.values()) mod.reload();
    }

    public static ConfigObject getModConfig(String modName) { return mods.get(modName); }
    public static Map<String, ConfigObject> getMods() { return mods; }
    public static void regModConfig(ConfigObject config) {
        mods.put(config.getModName(), config);
    }

    public static void loadCurrent() {
        String defaultCurrent = "{}";
        jsonCore = JSONConfigObject.openOrCreate(Objects.requireNonNull(SeasonsAPI.getOverworld().getServer()).getSavePath(WorldSavePath.ROOT).toAbsolutePath().resolve("seasons-current.json"), defaultCurrent);
        current = jsonCore.getJsonObject();
//        for (Config mod : mods.values()){
//            mod.reloadLang();
//            if (mod.config != null) mod.config.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
//        }
        SeasonsAPI.getLogger().info("Configs loaded!");
    }

    public static void setLang(String lang) {
        Config.getModConfig("API").getConfig().setValue("conf.lang", lang);
        updateLang(lang);
    }
    public static void updateLang(String lang) {
        currentLang = lang;
        for (ConfigObject mod : mods.values()) {
            mod.loadLang(lang);
        }
        Season.reloadDynamics();
        Weather.reloadDynamics();
        SeasonsAPI.getLogger().info("Lang changed to: {}", lang);
    }

    public static List<String> getListOfLangs() { return listOfLangs; }

    public static void reloadAll() {
        for (ConfigObject mod : mods.values()) {
            mod.reload();
        }
        SeasonsAPI.getLogger().info("Configs was reloaded!");
    }

    public static void saveCurrent() {
        jsonCore.writeJsonObject(current);
        jsonCore.save();
    }

    public static String getCurrent(String key) { return ""+currentMap.get(key).getValue(); }
    public static Object getCurrentTypedValue(String key) { return currentMap.get(key).getValue(); }
    public static ConfigValueObject<?> getCurrentTypedValueObject(String key) { return currentMap.get(key); }
    public static Object getCurrentTypedValueOrElse(String key, Object def) { return currentMap.containsKey(key) ? currentMap.get(key).getDefaultValue() : def; }
    public static Object getCurrentTypedDefaultValue(String key) { return currentMap.get(key).getDefaultValue(); }
    public static String getCurrentTypedValueDescription(String key) { return currentMap.get(key).getDescription(); }
    public static String getCurrentOrDefault(String key, String def) { return current.has(key)? current.get(key).getAsString() : def; }
    public static void writeCurrent(String key, Object value) {
        writeCurrent(key, value, "");
    }
    public static void writeCurrent(String key, Object value, String description) {
//        Object val = current.has(key) ? current.get(key) : value;
        if (!currentMap.containsKey(key)) {
            currentMap.put(key, new ConfigValueObject<>(value, value, "", description, (oldValue, newValue) -> current.addProperty(key, ""+newValue)));
            current.addProperty(key, ""+value);
        }
        else {
            if (value instanceof String s) currentMap.get(key).setValue(ConfigFileObject.parseValue(currentMap.get(key), s));
            else currentMap.get(key).setValue(value);
        }
    }
    public static void writeCurrentIfDoNotExists(String key) { writeCurrentIfDoNotExists(key, ""); }
    public static void writeCurrentIfDoNotExists(String key, String value) { writeCurrentIfDoNotExists(key, value, ""); }
    public static void writeCurrentIfDoNotExists(String key, Object value, String description) {
        if (!current.has(key)) writeCurrent(key, value, description);
    }
    public static void addCurrentValue(String key, Object def, String description) {
        if (!currentMap.containsKey(key)) currentMap.put(key, new ConfigValueObject<>(def, def, "", description, (oldValue, newValue) -> current.addProperty(key, ""+newValue)));
        if (current.has(key)) currentMap.get(key).setValue(ConfigFileObject.parseValue(currentMap.get(key), current.get(key).getAsString()));
        else {
            current.addProperty(key, ""+def);
        }
    }

    public static String getCurrentLang() { return currentLang; }
    public static Map<String, ConfigValueObject<?>> getCurrentMap() { return currentMap; }

    public static String getCopyright() {
        return "------------------------------------------------\nseasons-api\n------------------------------------------------\n" +
                "This mod was developed by analogy with the Spigot plugin \"Seasons\"\nspecifically for the private Minecraft server \"Zixa City\"\nby its administrator (kochkaev, aka kleverdi).\nThe idea of this mod was taken from Harieo.\n" +
                "------------------------------------------------\nHarieo on GitHub: https://github.com/Harieo/\nOriginal plugin on GitHub: https://github.com/Harieo/Seasons/\nOriginal plugin on SpigotMC: https://www.spigotmc.org/resources/seasons.39298/\n" +
                "------------------------------------------------\nCreated by @kochkaev\n  - GitHub: https://github.com/kochkaev/\n  - VK: https://vk.com/kleverdi/\n  - YouTube: https://youtube.com/@kochkaev/\n  - Contact email: kleverdi@vk.com\n"+
                "------------------------------------------------\nWARN: It's server-side mod.\n------------------------------------------------\n# # # # # # # # # # # # # # # # # # # # # # # # #\n";
    }
}

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com