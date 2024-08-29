package ru.kochkaev.api.seasons;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochkaev.api.seasons.command.SeasonsCommand;
import ru.kochkaev.api.seasons.integration.Environment;
import ru.kochkaev.api.seasons.integration.IntegrationManager;
import ru.kochkaev.api.seasons.integration.mod.ClothConfig;
import ru.kochkaev.api.seasons.integration.mod.PlaceholderAPI;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.service.Challenge;
import ru.kochkaev.api.seasons.service.Config;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.Format;

public class SeasonsAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger("Seasons");
    private static boolean isLoaded = false;
    private static PlaceholderAPI placeholderAPI = null;
    private static ClothConfig clothConfig = null;
    private static Environment environment;
    private static boolean isServer = true;

    public static void regEnvironment(Environment environment, boolean isServer) {
        SeasonsAPI.environment = environment;
        SeasonsAPI.isServer = isServer;
    }

    public static void onWorldStarted() {
        Register.register();
        Season.onServerStartup();
        Weather.onServerStartup();
        isLoaded = true;
        Challenge.updateChallengesInCurrentWeather();
        ChallengesTicker.changeWeather();
        ChallengesTicker.start();
    }
    public static void onWorldShutdown() {
        ChallengesTicker.stop();
        Weather.saveCurrentToConfig();
        Season.saveCurrentToConfig();
        Config.saveCurrent();
    }

    public static void start() {
        placeholderAPI = IntegrationManager.getPlaceholderAPIIfAvailable();
        clothConfig = IntegrationManager.getClothConfigIfAvailable();
        registerPlaceholders();
        Config.init__();
        Config.regModConfig(new ConfigObject("API", "en_US"));
        Register.registerAllInPackage("ru.kochkaev.api.seasons.config");
        Register.registerAllInPackage("ru.kochkaev.api.seasons.event");
        Register.registerAllInPackage("ru.kochkaev.api.seasons.example");
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> SeasonsCommand.register(dispatcher)));
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

    public static Logger getLogger() { return LOGGER; }
    public static boolean isLoaded() { return isLoaded; }
    public static Environment getEnvironment() { return environment; }
    public static boolean isServer() { return isServer; }
    public static void setLoaded(boolean loaded) { isLoaded = loaded; }
    public static PlaceholderAPI getPlaceholderAPI() {
        return placeholderAPI;
    }
    public static ClothConfig getClothConfig() {
        return clothConfig;
    }

}
