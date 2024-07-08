package ru.kochkaev.Seasons4Fabric;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochkaev.Seasons4Fabric.command.Seasons4FabricCommand;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.object.EventObject;
import ru.kochkaev.Seasons4Fabric.service.Challenge;
import ru.kochkaev.Seasons4Fabric.service.Event;
import ru.kochkaev.Seasons4Fabric.service.Season;
import ru.kochkaev.Seasons4Fabric.service.Weather;
import ru.kochkaev.Seasons4Fabric.util.ParseClassesInPackage;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Seasons");

	@Override
	public void onInitialize() {
		Config.init__();
		new ParseClassesInPackage<EventObject>("ru.kochkaev.Seasons4Fabric.event", Event::register);
		new ParseClassesInPackage<ChallengeObject>("ru.kochkaev.Seasons4Fabric.challenge", Challenge::register);
		Weather.restoreCurrentFromConfig();
		Season.restoreCurrentFromConfig();
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> Seasons4FabricCommand.register(dispatcher)));
		Challenge.updateChallengesInCurrentWeather();
		ChallengesTicker.start();

		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> onShutdown());
	}

	private void onShutdown() {
		ChallengesTicker.stop();
		Weather.saveCurrentToConfig();
		Season.saveCurrentToConfig();
		Config.saveCurrent();
	}

	public static Logger getLogger() { return LOGGER; }
}