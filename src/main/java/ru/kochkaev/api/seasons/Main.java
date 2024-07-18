package ru.kochkaev.api.seasons;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochkaev.api.seasons.command.Seasons4FabricCommand;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.config.DefaultTXTConfig;
import ru.kochkaev.api.seasons.config.lang.DefaultTXTLangRU;
import ru.kochkaev.api.seasons.service.Challenge;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Seasons");

	@Override
	public void onInitialize() {
		Config.init__();
		Config.regModConfig(new Config("API", new DefaultTXTConfig(), new DefaultTXTLangRU()));
		Register.registerAllInPackage("ru.kochkaev.seasons-api.event");
		Season.onServerStartup();
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> Seasons4FabricCommand.register(dispatcher)));
		Challenge.updateChallengesInCurrentWeather();
		ChallengesTicker.changeWeather();
		ChallengesTicker.start();

		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> onShutdown());
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			Register.register();
			Weather.onServerStartup(server.getOverworld());
		});
	}

	private void onShutdown() {
		ChallengesTicker.stop();
		Weather.saveCurrentToConfig();
		Season.saveCurrentToConfig();
		Config.saveCurrent();
	}

	public static Logger getLogger() { return LOGGER; }
}