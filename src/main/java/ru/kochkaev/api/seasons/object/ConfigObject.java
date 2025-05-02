package ru.kochkaev.api.seasons.object;

import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.provider.Config;

import java.util.HashMap;
import java.util.Map;

public class ConfigObject {

    private ConfigFileObject lang;
    private final Map<String, ConfigFileObject> langs = new HashMap<>();
    private final Map<String, ConfigFileObject> configs = new HashMap<>();
    private final String modName;
    private final String defaultLang;

    public ConfigObject(String modName, String defaultLang) {
        this.modName = modName;
        this.defaultLang = defaultLang;
//        if (SeasonsAPI.getClothConfig()!=null) ClothConfig.getClient().addConfigCategory(this);
        SeasonsAPI.getLogger().info("Loaded mod: " + modName);
    }

//    public void registerConfigObject(TXTConfigObject object) {
//        ConfigFileObject conf = new ConfigFileObject(object.getModName(), object.getFilename(), object.getType()) {
//            @Override
//            public void generate(ConfigContentObject content) {
//                object.generate();
//                getContent().putAll(object.getTypedValuesMap());
//                getContent().getQueue().addAll(object.getKeysQueue());
//            }
//            @Override
//            public void generateCreateIfDoNotExistsOpenAndUpdateIfLegacy() {
//                object.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
//            }
//            @Override
//            public void open() {
//                object.open();
//            }
//            @Override
//            public void reload() {
//                object.reload();
//            }
//        };
//        registerConfigObject(conf);
//    }
    public void registerConfigObject(ConfigFileObject object) {
        object.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
        if (object.getType().equals("config")) this.configs.put(object.getFilename(), object);
        else if (object.getType().equals("lang")) {
            String lang = object.getFilename();
            langs.put(lang, object);
            if (!Config.getListOfLangs().contains(lang)) Config.getListOfLangs().add(lang);
            if (lang.equals(Config.getCurrentLang())) this.lang = object;
            else object.close();
        }
    }

    public void reloadLang() {
        loadLang(Config.getCurrentLang());
    }
    public void loadLang(String langName) {
        lang = langs.containsKey(langName) ? langs.get(langName) : langs.get(defaultLang);
        if (lang != null) lang.generateCreateIfDoNotExistsOpenAndUpdateIfLegacy();
    }

    public void reload() {
        for (ConfigFileObject conf : configs.values()) {
            conf.reload();
        }
        reloadLang();
    }

    public String getModName() { return this.modName; }
    public ConfigFileObject getLang() { return lang; }
    public ConfigFileObject getConfig() { return configs.values().stream().findFirst().orElseThrow(); }
    public ConfigFileObject getConfig(String filename) { return configs.get(filename); }

    public Map<String, ConfigFileObject> getConfigs() {
        return configs;
    }
    public Map<String, ConfigFileObject> getLangs() {
        return langs;
    }
}
