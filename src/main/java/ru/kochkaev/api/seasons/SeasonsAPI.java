package ru.kochkaev.api.seasons;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochkaev.api.seasons.API.Events;
import ru.kochkaev.api.seasons.integration.IntegrationManager;
import ru.kochkaev.api.seasons.integration.mod.ClothConfig;
import ru.kochkaev.api.seasons.integration.mod.PlaceholderAPI;
import ru.kochkaev.api.seasons.loader.Loader;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.provider.Challenge;
import ru.kochkaev.api.seasons.provider.Config;
import ru.kochkaev.api.seasons.provider.Season;
import ru.kochkaev.api.seasons.provider.Weather;
import ru.kochkaev.api.seasons.util.Format;

public class SeasonsAPI {

    private static final SimpleLogger LOGGER = new SimpleLogger(LoggerFactory.getLogger("Seasons"));
    private static boolean isStarted = false;
    private static boolean isLoaded = false;
    private static PlaceholderAPI placeholderAPI = null;
    private static ClothConfig clothConfig = null;
    private static Loader loader;
    private static Events events;
    private static MinecraftServer server;
    private static ServerWorld world;

    public static void regLoader(Loader loader) {
        SeasonsAPI.loader = loader;
    }
    public static void regEvents(Events events) {
        SeasonsAPI.events = events;
    }

    public static void onWorldStarted(MinecraftServer server) {
        events.invokeBeforeAPIWorldInit(server);
        Register.registerAllInPackage("ru.kochkaev.api.seasons.example");
        SeasonsAPI.server = server;
        SeasonsAPI.world = server.getOverworld();
        SeasonsAPI.isStarted = true;
        Config.loadCurrent();
//        Config.addCurrentValue("enable", true, "Do seasons mod enabled in this world?\nThis setting requires to restart your world.");
        Config.addCurrentValue("season", "", "Current season (full path in seasons tree).\n'First-order season'->...->'Lower-order season'");
        Config.addCurrentValue("weather", "", "Current weather.");
        Config.addCurrentValue("previous_weather", "", "Previous weather.");
        Config.addCurrentValue("days_after_season_set", 0, "The count of days since the last installation of the first-order season.");
        Config.addCurrentValue("next_day_to_season_cycle", 0, "The day in order from the last setting of the first-order season, when the lower-order season will be replaced.");
        Config.addCurrentValue("seasons_cycle", "1:30:3", "Seasons cycle mode. This will be automatically updated after changing the relevant settings in API config.\n<maxOrderToCycle>:<daysPerSeason>:<subSeasonsPerSeason>\nPlease, DON'T TOUCH IT!");
        Config.addCurrentValue("players_show_actionbar", "", "Players who should be shown the action bar (or not shown if \"conf.enable.title.actionbarDefaultForAll\" is true). It can be changed by \"/seasons actionbar {on/off}\". Delimiter: \";\"");
        Config.saveCurrent();
//        if ((Boolean)Config.getCurrentTypedValue("enable")){
            Register.register();
            Season.onServerStartup();
            Weather.onServerStartup();
            isLoaded = true;
            Challenge.updateChallengesInCurrentWeather();
            ChallengesTicker.changeWeather();
            ChallengesTicker.start();
//        }
        events.invokeAfterAPIWorldInit(server);
    }
    public static void onWorldShutdown() {
        if (isLoaded){
            ChallengesTicker.stop();
            Weather.saveCurrentToConfig();
            Season.saveCurrentToConfig();
            Season.getTree().clear();
        }
        Config.saveCurrent();
//        while (ChallengesTicker.isTicking());
//        ChallengesTicker.close();
        isLoaded = false;
        isStarted = false;
    }

    public static void checkIntegrations() {
        placeholderAPI = IntegrationManager.getPlaceholderAPIIfAvailable();
        clothConfig = IntegrationManager.getClothConfigIfAvailable();
    }

    public static void start() {
        events.invokeBeforeAPIInit();
        Config.regModConfig(new ConfigObject("API", "en_US"));
        Register.registerAllAndApply("ru.kochkaev.api.seasons.config");
//        Config.initConfigObjects();
        Config.initConfigObjects();
        registerPlaceholders();
        loader.registerCommands();
        events.invokeAfterAPIInit();
    }

    private static void registerPlaceholders() {
        Format.regDynamicTextPlaceholder("weather", () -> Weather.getCurrent().getTextName());
        Format.regDynamicTextPlaceholder("season", () -> Season.getCurrent().getTextName());
        Format.regDynamicPlaceholder("lang", Config::getCurrentLang);
        Format.regDynamicPlaceholder("display-name", () -> Config.getModConfig("API").getLang().getString("lang.message.seasonsModDisplayName"));
        Format.regDynamicTextPlaceholder("weather-previous", () -> Weather.getPreviousCurrent().getTextName());
        Format.regDoubleParseDynamicPlaceholder("title-new-day", () -> Text.of(Config.getModConfig("API").getLang().getString("lang.message.messageNewDay")));
        Format.regDoubleParseDynamicPlaceholder("title-info", () -> Text.of(Config.getModConfig("API").getLang().getString("lang.message.currentInfo")));
        Format.regDoubleParseDynamicPlaceholder("actionbar", () -> Text.of(Config.getModConfig("API").getConfig().getString("conf.format.title.actionbar")));
    }

    public static SimpleLogger getLogger() { return LOGGER; }
    public static boolean isStarted() { return isStarted; }
    public static boolean isLoaded() { return isLoaded; }
    public static Loader getLoader() { return loader; }
    public static Events getEvents() { return events; }
    public static void setLoaded(boolean loaded) { isLoaded = loaded; }
    public static PlaceholderAPI getPlaceholderAPI() {
        return placeholderAPI;
    }
    public static ClothConfig getClothConfig() {
        return clothConfig;
    }
    public static MinecraftServer getServer() {
        return server;
    }
    public static ServerWorld getOverworld() {
        return world;
    }

}
