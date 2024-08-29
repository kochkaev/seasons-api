package ru.kochkaev.api.seasons.object;

import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.service.Config;

import java.util.HashMap;
import java.util.Map;

public class ConfigObject {

    private TXTConfigObject lang;
    private final Map<String, TXTConfigObject> langs = new HashMap<>();
    private final Map<String, TXTConfigObject> configs = new HashMap<>();
    private final String modName;
    private final String defaultLang;

    public ConfigObject(String modName, String defaultLang) {
        this.modName = modName;
        this.defaultLang = defaultLang;
        SeasonsAPI.getLogger().info("Loaded mod: {}", modName);
    }

    public void registerConfigObject(TXTConfigObject object) {
        object.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
        if (object.getType().equals("config")) this.configs.put(object.getFilename(), object);
        else if (object.getType().equals("lang")) {
            String lang = object.getFilename();
            langs.put(lang, object);
            if (!Config.getListOfLangs().contains(lang)) Config.getListOfLangs().add(lang);
        }
        object.close();
    }

    public void reloadLang() {
        loadLang(Config.getCurrent("language"));
    }
    public void loadLang(String langName) {
        lang = langs.containsKey(langName) ? langs.get(langName) : langs.get(defaultLang);
        lang.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
    }

    public void reload() {
        for (TXTConfigObject conf : configs.values()) {
            conf.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
        }
        reloadLang();
    }

    public String getModName() { return this.modName; }
    public TXTConfigObject getLang() { return lang; }
    public TXTConfigObject getConfig() { return configs.values().stream().findFirst().orElseThrow(); }
    public TXTConfigObject getConfig(String filename) { return configs.get(filename); }

}
