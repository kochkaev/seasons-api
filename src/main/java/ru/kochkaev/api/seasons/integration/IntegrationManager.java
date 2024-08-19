package ru.kochkaev.api.seasons.integration;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import ru.kochkaev.api.seasons.SeasonsAPI;

public class IntegrationManager {

    public static @Nullable PlaceholderAPI getPlaceholderAPIIfAvailable() {
        try {
            if (isModLoaded("placeholder-api")) return new PlaceholderAPI();
        } catch (Throwable t) {
            SeasonsAPI.getLogger().info("PlaceholderAPI don't matched, skipping");
        }
        return null;
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

}
