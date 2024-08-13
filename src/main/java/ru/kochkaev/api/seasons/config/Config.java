package ru.kochkaev.api.seasons.config;

import com.google.gson.JsonObject;
import ru.kochkaev.api.seasons.Main;
import ru.kochkaev.api.seasons.object.JSONConfigObject;
import ru.kochkaev.api.seasons.object.TXTConfigObject;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.functional.IFuncVoid;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private TXTConfigObject config;
    private TXTConfigObject lang;
    private final Map<String, TXTConfigObject> langs = new HashMap<>();
    private static JsonObject current = new JsonObject();
    private static JSONConfigObject jsonCore;
    private static final Map<String, Config> mods = new HashMap<>();
    private final String modName;
    private final String defaultLang;
    private static final List<String> allLangs =  new ArrayList<>();

    public Config(String modName, String defaultLang) {
        this.modName = modName;
        this.defaultLang = defaultLang;
        Main.getLogger().info("Loaded mod: {}", modName);
    }

    public void registerConfigObject(TXTConfigObject object) {
        object.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
        if (object.getType().equals("config")) this.config = object;
        else if (object.getType().equals("lang")) {
            String lang = object.getSubType();
            langs.put(lang, object);
            if (!allLangs.contains(lang)) allLangs.add(lang);
        }
        object.close();
    }

    public static void initConfigObjects(){
        for (Config mod : mods.values()) {
            if (mod.config != null) mod.config.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
            mod.reloadLang();
        }
    }

    public static Config getModConfig(String modName) { return mods.get(modName); }
    public static void regModConfig(Config config) {
        mods.put(config.getModName(), config);
    }
    public String getModName() { return this.modName; }

    public static void init__() {
        String defaultCurrent = "{ \"season\": NONE, \"weather\": NONE, \"previous-weather\": NONE, \"language\": EN_us }";
        jsonCore = JSONConfigObject.openOrCreate("Seasons/current", defaultCurrent);
        current = jsonCore.getJsonObject();
//        for (Config mod : mods.values()){
//            mod.reloadLang();
//            if (mod.config != null) mod.config.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
//        }
        Main.getLogger().info("Configs loaded!");
    }

    public void reloadLang() {
        loadLang(getCurrent("language"));
    }
    public void loadLang(String langName) {
        lang = langs.containsKey(langName) ? langs.get(langName) : langs.get(defaultLang);
        lang.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
    }

    public static void setLang(String lang) {
        writeCurrent("language", lang);
        saveCurrent();
        for (Config mod : mods.values()) {
            mod.loadLang(lang);
        }
        Season.reloadDynamics();
        Weather.reloadDynamics();
        Main.getLogger().info("Lang changed to: {}", lang);
    }

    public static List<String> getLangs() { return allLangs; }

    public static void reloadAll() {
        for (Config mod : mods.values()) {
            mod.reload();
        }
        Main.getLogger().info("Configs was reloaded!");
    }
    public void reload() {
        config.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
        reloadLang();
    }

    public static void saveCurrent() {
        jsonCore.writeJsonObject(current);
        jsonCore.save();
    }

    public static String getCurrent(String key) { return current.get(key).getAsString(); }
    public static void writeCurrent(String key, String value) { current.addProperty(key, value); }

    public TXTConfigObject getLang() { return lang; }
    public TXTConfigObject getConfig() { return config; }

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