package ru.kochkaev.api.seasons.config;

import ru.kochkaev.api.seasons.object.TXTConfigObject;

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
        addComment("* MESSAGES FORMAT");
        addComment("Chat message format");
        addComment("You can use placeholders: %seasonsModDisplayName% for insert seasons mod display name (from langs), %message% for insert message and & for insert color (replaces to paragraph symbol)");
        addValueAndCommentDefault("conf.format.chat.message", "%seasonsModDisplayName% &7• %message%");
        addComment("Actionbar title format");
        addComment("You can use placeholders: %season% for insert season name (from langs), %weather% for insert weather name (from langs) and & for insert color (replaces to paragraph symbol)");
        addValueAndCommentDefault("conf.format.title.actionbar", "%season% &r&7• %weather%");
        addComment("Title (on weather changed) format");
        addComment("You can use placeholders: %info% for insert info message (from langs, contains season and weather name), %messageNewDay% for insert title \"new day\" message (from langs) and & for insert color (replaces to paragraph symbol)");
        addValueAndCommentDefault("conf.format.title.title", "%messageNewDay%");
        addComment("Subtitle (on weather changed) format");
        addComment("You can use placeholders: %info% for insert info message (from langs, contains season and weather name), %messageNewDay% for insert title \"new day\" message (from langs) and & for insert color (replaces to paragraph symbol)");
        addValueAndCommentDefault("conf.format.title.subtitle", "%info%");
        addComment("Command feedback format");
        addComment("You can use placeholders: %message% for insert feedback message, & for insert color (replaces to paragraph symbol)");
        addValueAndCommentDefault("conf.format.chat.feedback", "&7&oSeasons feedback: %message%");

        // Enable/disable features
        addVoid();
        addComment("* ENABLE/DISABLE FEATURES");
        addComment("Do enables/disables actionbar titles");
        addValueAndCommentDefault("conf.enable.title.actionbar", "true");
        addComment("Do enables/disables titles and subtitles on weather changed");
        addValueAndCommentDefault("conf.enable.title.title", "false");
        addComment("Do enables/disables chat messages");
        addValueAndCommentDefault("conf.enable.chat.message", "true");

        // Settings
        addVoid();
        addComment("* SETTINGS");
        addComment("Tick of day starts");
        addValueAndCommentDefault("conf.tick.day.start", "0");
        addComment("Tick of day ends");
        addValueAndCommentDefault("conf.tick.day.end", "12542");
        addComment("Seconds per seasons-api tick");
        addValueAndCommentDefault("conf.tick.secondsPerTick", "1");
        addComment("Ticks before action starts (for example, damage)");
        addValueAndCommentDefault("conf.tick.ticksPerAction", "3");

    }
}
