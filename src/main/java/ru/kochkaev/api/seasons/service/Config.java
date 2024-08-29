package ru.kochkaev.api.seasons.service;

import com.google.gson.JsonObject;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.SeasonsAPIServer;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.object.JSONConfigObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private static JsonObject current = new JsonObject();
    private static JSONConfigObject jsonCore;
    private static final Map<String, ConfigObject> mods = new HashMap<>();
    private static final List<String> listOfLangs =  new ArrayList<>();

    public static void initConfigObjects(){
        for (ConfigObject mod : mods.values()) mod.reload();
    }

    public static ConfigObject getModConfig(String modName) { return mods.get(modName); }
    public static void regModConfig(ConfigObject config) {
        mods.put(config.getModName(), config);
    }

    public static void init__() {
        String defaultCurrent = "{ \"season\": NONE, \"weather\": NONE, \"previous-weather\": NONE, \"language\": EN_us }";
        jsonCore = JSONConfigObject.openOrCreate("Seasons/current", defaultCurrent);
        current = jsonCore.getJsonObject();
//        for (Config mod : mods.values()){
//            mod.reloadLang();
//            if (mod.config != null) mod.config.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
//        }
        SeasonsAPI.getLogger().info("Configs loaded!");
    }

    public static void setLang(String lang) {
        writeCurrent("language", lang);
        saveCurrent();
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
    public static void writeCurrent(String key, String value) { current.addProperty(key, value); }

    public static String getCopyright() {
        return "# ------------------------------------------------\n# seasons-api\n# ------------------------------------------------\n" +
                "# This mod was developed by analogy with the Spigot plugin \"Seasons\"\n# specifically for the private Minecraft server \"Zixa City\"\n# by its administrator (kochkaev, aka kleverdi).\n# The idea of this mod was taken from Harieo.\n" +
                "# ------------------------------------------------\n# Harieo on GitHub: https://github.com/Harieo/\n# Original plugin on GitHub: https://github.com/Harieo/Seasons/\n# Original plugin on SpigotMC: https://www.spigotmc.org/resources/seasons.39298/\n" +
                "# ------------------------------------------------\n# Created by @kochkaev\n#   - GitHub: https://github.com/kochkaev/\n#   - VK: https://vk.com/kleverdi/\n#   - YouTube: https://youtube.com/@kochkaev/\n#   - Contact email: kleverdi@vk.com\n"+
                "# ------------------------------------------------\n# WARN: It's server-side mod.\n# ------------------------------------------------\n# # # # # # # # # # # # # # # # # # # # # # # # # #\n";
    }
}

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com