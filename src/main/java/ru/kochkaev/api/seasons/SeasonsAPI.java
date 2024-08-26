package ru.kochkaev.api.seasons;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochkaev.api.seasons.command.SeasonsCommand;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.service.Config;
import ru.kochkaev.api.seasons.integration.IntegrationManager;
import ru.kochkaev.api.seasons.integration.PlaceholderAPI;
import ru.kochkaev.api.seasons.service.Challenge;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.Format;

public class SeasonsAPI implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("Seasons");
	private static boolean isLoaded = false;
	private static PlaceholderAPI placeholderAPI = null;
	private static MinecraftServer server;
	private static ServerWorld overworld;

	@Override
	public void onInitialize() {
		Config.init__();
		Config.regModConfig(new ConfigObject("API", "en_US"));
		Register.registerAllInPackage("ru.kochkaev.api.seasons.config");
		Register.registerAllInPackage("ru.kochkaev.api.seasons.event");
		Register.registerAllInPackage("ru.kochkaev.api.seasons.example");
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> SeasonsCommand.register(dispatcher)));
		ServerLifecycleEvents.SERVER_STARTED.register(this::onStarted);
		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> onShutdown());
	}

	private void onStarted(MinecraftServer server) {
		SeasonsAPI.server = server;
		SeasonsAPI.overworld = server.getOverworld();
		Register.register();
		Season.onServerStartup();
		Weather.onServerStartup();
		placeholderAPI = IntegrationManager.getPlaceholderAPIIfAvailable();
		registerPlaceholders();
		isLoaded = true;
		Challenge.updateChallengesInCurrentWeather();
		ChallengesTicker.changeWeather();
		ChallengesTicker.start();
	}
	private void onShutdown() {
		ChallengesTicker.stop();
		Weather.saveCurrentToConfig();
		Season.saveCurrentToConfig();
		Config.saveCurrent();
	}

	public static Logger getLogger() { return LOGGER; }
	public static boolean isLoaded() { return isLoaded; }
	public static void setLoaded(boolean loaded) { isLoaded = loaded; }
	public static PlaceholderAPI getPlaceholderAPI() {
		return placeholderAPI;
	}
	public static MinecraftServer getServer() {
		return server;
	}
	public static ServerWorld getOverworld() {
		return overworld;
	}

	private static void registerPlaceholders() {
		Format.regDynamicPlaceholder("weather", () -> Weather.getCurrent().getName());
		Format.regDynamicPlaceholder("season", () -> Season.getCurrent().getName());
		Format.regDynamicPlaceholder("lang", () -> Config.getCurrent("language"));
		Format.regDynamicPlaceholder("display-name", () -> Config.getModConfig("API").getLang().getString("lang.message.seasonsModDisplayName"));
		Format.regDynamicPlaceholder("weather-previous", () -> Weather.getPreviousCurrent().getName());
		Format.regDoubleParseDynamicPlaceholder("title-new-day", () -> Text.of(Config.getModConfig("API").getLang().getString("lang.message.messageNewDay")));
		Format.regDoubleParseDynamicPlaceholder("title-info", () -> Text.of(Config.getModConfig("API").getLang().getString("lang.message.currentInfo")));
		Format.regDoubleParseDynamicPlaceholder("actionbar", () -> Text.of(Config.getModConfig("API").getConfig().getString("conf.format.title.actionbar")));
	}
}