package ru.kochkaev.api.seasons.config.lang;

import ru.kochkaev.api.seasons.config.DefaultTXTConfig;
import ru.kochkaev.api.seasons.object.ConfigContentObject;
import ru.kochkaev.api.seasons.object.ConfigFileObject;
import ru.kochkaev.api.seasons.provider.Config;

public class DefaultTXTLangEN extends ConfigFileObject {

    private static final String copyright = Config.getCopyright() + "\nIt's mod lang!\n" +
            "\n** NAME = Display name of the Weather/Season\n** MESSAGE = Message, sends to chat on trigger\n\nOther config files:\n  - current.json - information about the current Weather/Season,\n    is updated when the server is turned off.\n  - lang - directory, contains translations of mod names/messages.\n" +
            "\nYou can use placeholders:\n  - %message% - will be replaced to message in specific context" +
            "\n  - %seasons:weather% - insert current weather name (from langs)\n  - %seasons:weather-previous% - insert previous weather name (from langs)\n  - %seasons:season% - insert current season name (from langs)" +
            "\n  - %seasons:lang% - insert current lang key\n  - %seasons:display-name% - insert Seasons mod display name (lang.message.seasonsModDisplayName in langs)" +
            "\n  - %seasons:title-new-day% - insert new day title message (from langs)\n  - %seasons:title-info% - insert new day info title message (from langs)" +
            "\n  - %seasons:actionbar% - insert actionbar title message format\n  - And others if you have installed PlaceholderAPI." +
            "\nYou can format your messages using MiniMessage! Details: https://docs.advntr.dev/minimessage/format.html";
    public DefaultTXTLangEN() {
        super("API", "en_US", "lang", copyright, 2);
    }

    public void generate(ConfigContentObject content) {
        content
                .addTextValue("lang.message.seasonsModDisplayName", "<yellow><bold>Seasons", "Seasons mod name (used in chat messages as %seasons:display-name% placeholder)")
                .addValue("lang.message.messageNewDay", "<yellow><bold>That's a new day!", "New day title message (used in titles as %seasons:title-new-day% placeholder)")
                .addValue("lang.message.currentInfo", "%seasons:season%. <gray>Today's %seasons:weather%", "Current weather and season title message (used in titles as %seasons:title-info% placeholder)");
    }

    @Override
    public Boolean update(ConfigContentObject content, Integer targetVersion, Integer currentVersion) {
        return DefaultTXTConfig.configUpdate(content, targetVersion, currentVersion);
    }
}
