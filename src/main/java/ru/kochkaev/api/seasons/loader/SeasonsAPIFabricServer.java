package ru.kochkaev.api.seasons.loader;

import net.fabricmc.api.DedicatedServerModInitializer;
import ru.kochkaev.api.seasons.SeasonsAPI;

public class SeasonsAPIFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        SeasonsAPI.checkIntegrations();
        SeasonsAPI.start();
    }
}
