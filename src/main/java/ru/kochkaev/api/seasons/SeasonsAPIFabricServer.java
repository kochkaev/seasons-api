package ru.kochkaev.api.seasons;

import net.fabricmc.api.DedicatedServerModInitializer;

public class SeasonsAPIFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        SeasonsAPI.checkIntegrations();
        SeasonsAPI.start();
    }
}
