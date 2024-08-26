package ru.kochkaev.api.seasons.config.lang;

import ru.kochkaev.api.seasons.service.Config;
import ru.kochkaev.api.seasons.object.TXTConfigObject;

public class DefaultTXTLangEN extends TXTConfigObject {

    public DefaultTXTLangEN() {
        super("API", "en_US", "lang");
    }

    public void generate() {

        String copyright = Config.getCopyright() + "#\n# It's mod lang!\n#" +
                "\n# ** NAME = Display name of the Weather/Season\n# ** MESSAGE = Message, sends to chat on trigger\n#\n# Other config files:\n#   - current.json - information about the current Weather/Season,\n#     is updated when the server is turned off.\n#   - lang - directory, contains translations of mod names/messages.\n#" +
                "\n# You can use placeholders: \n#   - & - insert color (replaces to paragraph symbol)\n#   - %message% - will be replaced to message in specific context" +
                "\n#   - %seasons:weather% - insert current weather name (from langs)\n#   - %seasons:weather-previous% - insert previous weather name (from langs)\n#   - %seasons:season% - insert current season name (from langs)" +
                "\n#   - %seasons:lang% - insert current lang key\n#   - %seasons:display-name% - insert Seasons mod display name (lang.message.seasonsModDisplayName in langs)" +
                "\n#   - %seasons:title-new-day% - insert new day title message (from langs)\n#   - %seasons:title-info% - insert new day info title message (from langs)" +
                "\n#   - %seasons:actionbar% - insert actionbar title message format\n#   - And others if you have installed PlaceholderAPI\n\n\n";
        addString(copyright);

       addComment("Seasons mod name (used in chat messages as %seasons:display-name% placeholder)");
       addValueAndCommentDefault("lang.message.seasonsModDisplayName", "&e&lSeasons");
       addComment("New day title message (used in titles as %seasons:title-new-day% placeholder)");
       addValueAndCommentDefault("lang.message.messageNewDay", "&e&bThat's new day!");
       addComment("Current weather and season title message (used in titles as %seasons:title-info% placeholder)");
       addValueAndCommentDefault("lang.message.currentInfo", "%season%. &7Today's %weather%");
    }
}
