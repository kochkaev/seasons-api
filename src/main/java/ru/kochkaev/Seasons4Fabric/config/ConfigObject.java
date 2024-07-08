package ru.kochkaev.Seasons4Fabric.config;

public class ConfigObject {

    private final TXTMapObject config;

    public String getString(String key) {
        return config.get(key);
    }
    public int getInt(String key) {
        return Integer.parseInt(config.get(key));
    }
    public long getLong(String key) {
        return Long.parseLong(config.get(key));
    }

    public void reload() { config.reload(); }

    ConfigObject(TXTMapObject map) { this.config = map; }
}
