package ru.kochkaev.api.seasons.config;

import ru.kochkaev.api.seasons.object.TXTConfigObject;
import ru.kochkaev.api.seasons.service.Config;

public class DefaultTXTConfig extends TXTConfigObject{

    public DefaultTXTConfig() {
        super("API", "config", "config");
    }

    public void generate() {

        String copyright = Config.getCopyright() + "#\n# It's mod config!\n#" +
                "\n# Other config files:\n#   - current.json - information about the current Weather/Season,\n#     is updated when the server is turned off.\n#   - lang - directory, contains translations of mod names/messages.\n\n\n";
        addString(copyright);

        // Messages format
        addVoid();
        addHeader("MESSAGES FORMAT");
        addString("# You can use placeholders: \n#   - & - insert color (replaces to paragraph symbol)\n#   - %message% - will be replaced to message in specific context" +
                "\n#   - %seasons:weather% - insert current weather name (from langs)\n#   - %seasons:weather-previous% - insert previous weather name (from langs)\n#   - %seasons:season% - insert current season name (from langs)" +
                "\n#   - %seasons:lang% - insert current lang key\n#   - %seasons:display-name% - insert Seasons mod display name (lang.message.seasonsModDisplayName in langs)" +
                "\n#   - %seasons:title-new-day% - insert new day title message (from langs)\n#   - %seasons:title-info% - insert new day info title message (from langs)" +
                "\n#   - %seasons:actionbar% - insert actionbar title message format\n#   - And others if you have installed PlaceholderAPI\n");
        addComment("Chat message format");
        addComment("You can use %message% for insert message");
        addTypedValueAndCommentDefault("conf.format.chat.message", "%seasons:display-name% &7• %message%");
        addComment("Actionbar title format");
        addTypedValueAndCommentDefault("conf.format.title.actionbar", "%seasons:season% &r&7• %seasons:weather%");
        addComment("Title (on weather changed) format");
        addTypedValueAndCommentDefault("conf.format.title.title", "%seasons:title-new-day%");
        addComment("Subtitle (on weather changed) format");
        addTypedValueAndCommentDefault("conf.format.title.subtitle", "%seasons:title-info%");
        addComment("Command feedback format");
        addComment("You can use %message% for insert feedback message");
        addTypedValueAndCommentDefault("conf.format.chat.feedback", "&7&oSeasons feedback: %message%");

        // Enable/disable features
        addVoid();
        addHeader("ENABLE/DISABLE FEATURES");
        addComment("Do enables/disables actionbar titles");
        addTypedValueAndCommentDefault("conf.enable.title.actionbar", true);
        addComment("Do enables/disables titles and subtitles on weather changed");
        addTypedValueAndCommentDefault("conf.enable.title.title", false);
        addComment("Do enables/disables chat messages");
        addTypedValueAndCommentDefault("conf.enable.chat.message", true);

        // Settings
        addVoid();
        addHeader("SETTINGS");
        addComment("Tick of day starts");
        addTypedValueAndCommentDefault("conf.tick.day.start", 0L);
        addComment("Tick of day ends");
        addTypedValueAndCommentDefault("conf.tick.day.end", 12542L);
        addComment("Seconds per seasons-api tick");
        addTypedValueAndCommentDefault("conf.tick.secondsPerTick", 1);
        addComment("Ticks before action starts (for example, damage)");
        addTypedValueAndCommentDefault("conf.tick.ticksPerAction", 3);

        // Language
//        addVoid();
//        addHeader("LANGUAGE");
//        addComment("Current language of seasons mod (currently works only in game)");
//        addTypedValueAndCommentDefault("conf.lang", "en_US");

    }
}
