package ru.kochkaev.api.seasons.integration.mod;

import com.sun.jna.platform.unix.X11;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import ru.kochkaev.api.seasons.object.ConfigObject;

public abstract class ClothConfig {

    private static ClothConfig client;
    private static ClothConfig instance;

    public abstract Object getConfigScreen(Object parent);
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
                public ConfigBuilder getConfigScreen(Object parent) {
                    return null;
                }
//                @Override
//                public void addConfigCategory(ConfigObject config) {}
            };
        }
        return instance;
    }

}
