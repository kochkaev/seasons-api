package ru.kochkaev.api.seasons.config.lang;

import ru.kochkaev.api.seasons.object.ConfigContentObject;
import ru.kochkaev.api.seasons.object.ConfigFileObject;
import ru.kochkaev.api.seasons.provider.Config;

public class DefaultTXTLangRU extends ConfigFileObject {

    private static final String copyright = Config.getCopyright() + "\nIt's mod lang!\n" +
            "\n** NAME = Display name of the Weather/Season\n** MESSAGE = Message, sends to chat on trigger\n#\nOther config files:\n  - current.json - information about the current Weather/Season,\n    is updated when the server is turned off.\n  - lang - directory, contains translations of mod names/messages.\n"+
            "\n\nYou can use placeholders: \n  - & - insert color (replaces to paragraph symbol)\n  - %message% - will be replaced to message in specific context" +
            "\n  - %seasons:weather% - insert current weather name (from langs)\n  - %seasons:weather-previous% - insert previous weather name (from langs)\n  - %seasons:season% - insert current season name (from langs)" +
            "\n  - %seasons:lang% - insert current lang key\n  - %seasons:display-name% - insert Seasons mod display name (lang.message.seasonsModDisplayName in langs)" +
            "\n  - %seasons:title-new-day% - insert new day title message (from langs)\n  - %seasons:title-info% - insert new day info title message (from langs)" +
            "\n  - %seasons:actionbar% - insert actionbar title message format\n  - And others if you have installed PlaceholderAPI";
    public DefaultTXTLangRU() {
        super("API", "ru_RU", "lang", copyright);
    }

    public void generate(ConfigContentObject content) {
        content
                .addValue("lang.message.seasonsModDisplayName", "&e&lСезоны", "Seasons mod name (used in chat messages as %seasons:display-name% placeholder)")
                .addValue("lang.message.messageNewDay", "&e&bНаступил новый день!", "New day title message (used in titles as %seasons:title-new-day% placeholder)")
                .addValue("lang.message.currentInfo", "%seasons:season%. &7Сегодня %seasons:weather%", "Current weather and season title message (used in titles as %seasons:title-info% placeholder)");
    }
}
