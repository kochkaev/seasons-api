package ru.kochkaev.api.seasons.config;

import ru.kochkaev.api.seasons.object.ConfigContentObject;
import ru.kochkaev.api.seasons.object.ConfigFileObject;
import ru.kochkaev.api.seasons.provider.Config;
import ru.kochkaev.api.seasons.provider.Season;

public class DefaultTXTConfig extends ConfigFileObject {

    private static final String copyright = Config.getCopyright() + "\nIt's mod config!\n\nOther config files:\n  - current.json - information about the current Weather/Season,\n    is updated when the server is turned off.\n  - lang - directory, contains translations of mod names/messages."+
            "\n\nYou can use placeholders: \n  - & - insert color (replaces to paragraph symbol)\n  - %message% - will be replaced to message in specific context" +
            "\n  - %seasons:weather% - insert current weather name (from langs)\n  - %seasons:weather-previous% - insert previous weather name (from langs)\n  - %seasons:season% - insert current season name (from langs)" +
            "\n  - %seasons:lang% - insert current lang key\n  - %seasons:display-name% - insert Seasons mod display name (lang.message.seasonsModDisplayName in langs)" +
            "\n  - %seasons:title-new-day% - insert new day title message (from langs)\n  - %seasons:title-info% - insert new day info title message (from langs)" +
            "\n  - %seasons:actionbar% - insert actionbar title message format\n  - And others if you have installed PlaceholderAPI";
    public DefaultTXTConfig() {
        super("API", "config", "config", copyright);
    }

    public void generate(ConfigContentObject content) {
        content
                // Messages format
                .addHeader("MESSAGES FORMAT")
                .addValue("conf.format.chat.message", "%seasons:display-name% &7• %message%", "Chat message format" +
                        "\nYou can use %message% for insert message")
                .addValue("conf.format.title.actionbar", "%seasons:season% &r&7• %seasons:weather%", "Actionbar title format")
                .addValue("conf.format.title.title", "%seasons:title-new-day%", "Title (on weather changed) format")
                .addValue("conf.format.title.subtitle", "%seasons:title-info%", "Subtitle (on weather changed) format")
                .addValue("conf.format.chat.feedback", "&7&oSeasons feedback: %message%", "Command feedback format" +
                        "\nYou can use %message% for insert feedback message")
                // Enable/disable features
                .addHeader("ENABLE/DISABLE FEATURES")
                .addValue("conf.enable.title.actionbar", true, "Do enables/disables actionbar titles")
                .addValue("conf.enable.title.actionbarDefaultForAll", true, "If true all players will see the information in the actionbar by default.")
                .addValue("conf.enable.title.title", false, "Do enables/disables titles and subtitles on weather changed")
                .addValue("conf.enable.chat.message", true, "Do enables/disables chat messages")
                // Settings
                .addHeader("SETTINGS")
                .addValue("conf.tick.day.start", 0L, "Tick of day starts")
                .addValue("conf.tick.day.end", 12542L, "Tick of day ends")
                .addValue("conf.tick.ticksPerChallengeTick", 20, "Minecraft ticks per challenges tick (20 Minecraft ticks in 1 second)")
                .addValue("conf.tick.challengeTicksPerAction", 3, "Challenges ticks before action starts (for example, damage)")
                // Seasons cycle
                .addHeader("SEASONS CYCLE")
                .addValue("conf.seasonsCycle.doSeasonsCycle", true, "Do cycle the first order seasons for a daysPerSeason (<duration_of_parent_season>/subSeasonPerSeason for a sub seasons) Minecraft days?" +
                        "\nThe highest (first) order seasons are seasons that are not a sub season for any other season." +
                        "\nThe second order seasons are seasons that are sub seasons for the first order, but parent for the third order. Etc." +
                        "\nThe parent season is the season of the order above, for which this season is a sub season.")
                .addValue("conf.seasonsCycle.maxOrderToCycle", 1, "", "Number of the order from which all orders bellow are able to cycle seasons.", (oldValue, newValue) -> Season.reloadCycleTimer())
                .addValue("conf.seasonsCycle.daysPerSeason", 30, "", "The time of the season change to the next one (in Minecraft days)", (oldValue, newValue) -> Season.reloadCycleTimer())
                .addValue("conf.seasonsCycle.subSeasonsPerSeason", 3, "", "Count of sub seasons possible to set during the season.", (oldValue, newValue) -> Season.reloadCycleTimer())
                // Language
                .addHeader("LANGUAGE")
                .addDynamicSelectionDropdown("conf.lang", Config::getListOfLangs, "en_US", "", "Current language of seasons mod", (oldValue, newValue) -> Config.updateLang(newValue))
                // Developer
                .addHeader("DEVELOPER")
                .addValue("conf.dev.logging", false, "Do enables/disables advanced logging");

    }
}
