package ru.kochkaev.api.seasons.integration.mod;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import ru.kochkaev.api.seasons.object.ConfigObject;

public abstract class ClothConfig {

    private static ClothConfig client;
    private static ClothConfig instance;

    public abstract Object getConfigScreen(Object parent, ConfigObject priority);
//    public abstract void addConfigCategory(ConfigObject config);

    public static ClothConfig getClient() {
        return client;
    }
    public static void setClient(ClothConfig client) {
        ClothConfig.client = client;
    }
    public static ClothConfig getInstance() {
        if (instance == null) {
            instance = new ClothConfig() {
                @Override
                public ConfigBuilder getConfigScreen(Object parent, ConfigObject priority) {
                    return null;
                }
//                @Override
//                public void addConfigCategory(ConfigObject config) {}
            };
        }
        return instance;
    }

}
