package ru.kochkaev.Seasons4Fabric;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochkaev.Seasons4Fabric.Commands.Seasons4FabricCommand;
import ru.kochkaev.Seasons4Fabric.Config.Config;
//import ru.kochkaev.Seasons4Fabric.Config.OldConfig;
import ru.kochkaev.Seasons4Fabric.Service.Season;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("Seasons4Fabric");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//OldConfig.registerConfigs();
		Config.init__();
		Weather.restoreCurrentFromConfig();
		Season.restoreCurrentFromConfig();
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> Seasons4FabricCommand.register(dispatcher)));

		LOGGER.info("Hello Fabric world!");

		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> onShutdown());
	}

	private void onShutdown() {
		Weather.saveCurrentToConfig();
		Season.saveCurrentToConfig();
		//OldConfig.closeJson();
		Config.close__();
	}

	public static Logger getLogger() { return LOGGER; }
}