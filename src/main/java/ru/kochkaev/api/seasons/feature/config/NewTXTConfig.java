package ru.kochkaev.api.seasons.feature.config;

import ru.kochkaev.api.seasons.feature.config.annotation.*;
import ru.kochkaev.api.seasons.provider.Config;

public class NewTXTConfig {

    @TXTConfigIgnore
    private static final String copyright = Config.getCopyright() + "\nIt's mod config!\n\nOther config files:\n  - current.json - information about the current Weather/Season,\n    is updated when the server is turned off.\n  - lang - directory, contains translations of mod names/messages."+
            "\n\nYou can use placeholders: \n  - & - insert color (replaces to paragraph symbol)\n  - %message% - will be replaced to message in specific context" +
            "\n  - %seasons:weather% - insert current weather name (from langs)\n  - %seasons:weather-previous% - insert previous weather name (from langs)\n  - %seasons:season% - insert current season name (from langs)" +
            "\n  - %seasons:lang% - insert current lang key\n  - %seasons:display-name% - insert Seasons mod display name (lang.message.seasonsModDisplayName in langs)" +
            "\n  - %seasons:title-new-day% - insert new day title message (from langs)\n  - %seasons:title-info% - insert new day info title message (from langs)" +
            "\n  - %seasons:actionbar% - insert actionbar title message format\n  - And others if you have installed PlaceholderAPI";

    @TXTConfigHeader("MESSAGES FORMAT")
    @TXTConfigDescription("Chat message format" +
            "\nYou can use %message% for insert message")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.format.chat.message")
    public final String messageFormat = "%seasons:display-name% &7• %message%";

    @TXTConfigDescription("Actionbar title format")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.format.title.actionbar")
    public final String actionbarFormat = "%seasons:season% &r&7• %seasons:weather%";

    @TXTConfigDescription("Title (on weather changed) format")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.format.title.title")
    public final String titleFormat = "%seasons:title-new-day%";

    @TXTConfigDescription("Subtitle (on weather changed) format")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.format.title.subtitle")
    public final String subtitleFormat = "%seasons:title-info%";

    @TXTConfigDescription("Command feedback format" +
            "\nYou can use %message% for insert feedback message")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.format.chat.feedback")
    public final String chatFeedbackFormat = "&7&oSeasons feedback: %message%";


    @TXTConfigHeader("ENABLE/DISABLE FEATURES")
    @TXTConfigDescription("Do enables/disables actionbar titles")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.enable.title.actionbar")
    public final boolean actionbarEnabled = true;

    @TXTConfigDescription("Do enables/disables titles and subtitles on weather changed")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.enable.title.title")
    public final boolean titleEnabled = false;

    @TXTConfigDescription("Do enables/disables chat messages")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.enable.chat.message")
    public final boolean chatMessagesEnabled = true;


    @TXTConfigHeader("SETTINGS")
    @TXTConfigDescription("Tick of day starts")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.tick.day.start")
    public final Long dayStartsOn = 0L;

    @TXTConfigDescription("Tick of day ends")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.tick.day.end")
    public final Long dayEndsOn = 12542L;

    @TXTConfigDescription("Minecraft ticks per challenges tick (20 Minecraft ticks in 1 second)")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.tick.ticksPerChallengeTick")
    public final Integer ticksPerChallengeTick = 20;

    @TXTConfigDescription("Challenges ticks before action starts (for example, damage)")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.tick.challengeTicksPerAction")
    public final Integer challengeTicksPerAction = 3;


    @TXTConfigHeader("SEASONS CYCLE")
    @TXTConfigDescription("Do cycle the first order seasons for a daysPerSeason (<duration_of_parent_season>/subSeasonPerSeason for a sub seasons) Minecraft days?" +
            "\nThe highest (first) order seasons are seasons that are not a sub season for any other season." +
            "\nThe second order seasons are seasons that are sub seasons for the first order, but parent for the third order. Etc." +
            "\nThe parent season is the season of the order above, for which this season is a sub season.")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.seasonsCycle.doSeasonsCycle")
    public final boolean doSeasonsCycle = true;

    @TXTConfigDescription("Number of the order from which all orders bellow are able to cycle seasons.")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.seasonsCycle.maxOrderToCycle")
    public final Integer maxOrderToSeasonsCycle = 1;

    @TXTConfigDescription("The time of the season change to the next one (in Minecraft days)")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.seasonsCycle.daysPerSeason")
    public final Integer daysPerSeason = 30;

    @TXTConfigDescription("Count of sub seasons possible to set during the season.")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.seasonsCycle.subSeasonsPerSeason")
    public final Integer subSeasonsPerSeason = 3;


    @TXTConfigHeader("LANGUAGE")
    @TXTConfigDescription("Current language of seasons mod")
    @TXTConfigAutoAddMeta
    @TXTConfigCustomKey("conf.lang")
    public final String language = "en_US";
//    .addDynamicSelectionDropdown("conf.lang", Config::getListOfLangs, "en_US", "", "Current language of seasons mod", (oldValue, newValue) -> Config.updateLang(newValue));
}
