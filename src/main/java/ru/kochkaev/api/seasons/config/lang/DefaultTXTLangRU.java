package ru.kochkaev.api.seasons.config.lang;

import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.TXTConfigObject;

public class DefaultTXTLangRU extends TXTConfigObject {
    
    public DefaultTXTLangRU() {
        super("API", "RU_ru", "lang");
    }

    public void generate() {

        String copyright = Config.getCopyright() + "#\n# It's mod lang!\n#" +
                "\n# ** NAME = Display name of the Weather/Season\n# ** MESSAGE = Message, sends to chat on trigger\n#\n# Placeholders:\n#   - %season% - replaces to current season.\n" +
                "#   - %weather% - replaces to current weather.\n#   - & - replaces to paragraph symbol (insert color).\n#\n# Other config files:\n#   - current.json - information about the current Weather/Season,\n#     is updated when the server is turned off.\n#   - lang - directory, contains translations of mod names/messages.\n\n\n";
        addString(copyright);

       addComment("Seasons mod name (used in chat messages as %seasonsModDisplayName% placeholder)");
       addValueAndCommentDefault("lang.message.seasonsModDisplayName", "&e&lСезоны");
        addComment("New day title message (used in titles as %messageNewDay% placeholder)");
        addValueAndCommentDefault("lang.message.messageNewDay", "&e&bНаступил новый день!");
        addComment("Current weather and season title message (used in titles as %info% placeholder)");
        addValueAndCommentDefault("lang.message.currentInfo", "%season%. &7Сегодня %weather%");
    }
}
