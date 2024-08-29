package ru.kochkaev.api.seasons.integration.mod;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import ru.kochkaev.api.seasons.SeasonsAPI;

public class ModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            if (SeasonsAPI.getClothConfig()!=null) {
                return (Screen) ClothConfig.getClient().getConfigScreen(parent);
                // Return the screen here with the one you created from Cloth Config Builder
            }
            return null;
        };
    }

}
