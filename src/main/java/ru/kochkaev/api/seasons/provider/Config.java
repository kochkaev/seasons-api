package ru.kochkaev.api.seasons.provider;

import com.google.gson.JsonObject;
import net.minecraft.util.WorldSavePath;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.object.JSONConfigObject;

import java.util.*;

public final class Config {

    private static JsonObject current = new JsonObject();
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

    public static String getCurrent(String key) { return current.get(key).getAsString(); }
    public static String getCurrentOrDefault(String key, String def) { return current.has(key)? current.get(key).getAsString() : def; }
    public static void writeCurrent(String key, String value) { current.addProperty(key, value); }
    public static void writeCurrentIfDoNotExists(String key) { if (!current.has(key)) current.addProperty(key, ""); }
    public static void writeCurrentIfDoNotExists(String key, String value) { if (!current.has(key)) current.addProperty(key, value); }

    public static String getCurrentLang() { return currentLang; }

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