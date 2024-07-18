package ru.kochkaev.api.seasons.config.lang;

import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.TXTConfigObject;

public class DefaultTXTLangRU extends TXTConfigObject {
    
    public DefaultTXTLangRU() {
        super("", "RU_ru", "lang");
        generate();
    }

    private void generate() {

        String copyright = Config.getCopyright() + "#\n# It's mod lang!\n#" +
                "\n# ** NAME = Display name of the Weather/Season\n# ** MESSAGE = Message, sends to chat on trigger\n#\n# Placeholders:\n#   - %season% - replaces to current season.\n" +
                "#   - %weather% - replaces to current weather.\n#   - & - replaces to \"§\" (colour codes).\n#\n# Other config files:\n#   - current.json - information about the current Weather/Season,\n#     is updated when the server is turned off.\n#   - lang - directory, contains translations of mod names/messages.\n\n\n";
        addString(copyright);

        /// SEASON
        addComment("* SEASON");
        // Winter
        addComment("Winter");
        addValueAndCommentDefault("lang.season.winter.name", "&bЗима");
        addValueAndCommentDefault("lang.season.winter.message", "&eДа свершится новогоднее чудо! Наступила %season%");
        // Spring
        addComment("Spring");
        addValueAndCommentDefault("lang.season.spring.name", "&2Весна");
        addValueAndCommentDefault("lang.season.spring.message", "&eС глаз долой весь снег! Наступила %season%");
        // Summer
        addComment("Summer");
        addValueAndCommentDefault("lang.season.summer.name", "&aЛето");
        addValueAndCommentDefault("lang.season.summer.message", "&eНастало время пекла! Наступило %season%");
        // Fall
        addComment("Fall");
        addValueAndCommentDefault("lang.season.fall.name", "&6Осень");
        addValueAndCommentDefault("lang.season.fall.message", "&eУнылая пора! Очей очарованье! Наступила %season%");

        /// WEATHER
        addVoid();
        addComment("* WEATHER");
        // Night
        addComment("Night");
        addValueAndCommentDefault("lang.weather.night.name", "&7Ночь");
        addValueAndCommentDefault("lang.weather.night.message", "&7Кажется темнеет... Наступает %weather%");
        // Snowy
        addComment("Snowy");
        addValueAndCommentDefault("lang.weather.snowy.name", "&7Снежно");
        addValueAndCommentDefault("lang.weather.snowy.message", "&7Белый туман одеялом ложится на крыши и цветы. Сегодня %weather%");
        // Freezing
        addComment("Freezing");
        addValueAndCommentDefault("lang.weather.freezing.name", "&9Морозно");
        addValueAndCommentDefault("lang.weather.freezing.message", "&3Вся вода замерзает! Сегодня %weather%");
        // Stormy
        addComment("Stormy");
        addValueAndCommentDefault("lang.weather.stormy.name", "&cШторм");
        addValueAndCommentDefault("lang.weather.stormy.message", "&cГремит январская вьюга! Сегодня %weather%");
        // Cold
        addComment("Cold");
        addValueAndCommentDefault("lang.weather.cold.name", "&9Холодно");
        addValueAndCommentDefault("lang.weather.cold.message", "&3На окнах появляется иней. Сегодня %weather%");
        // Warm
        addComment("Warm");
        addValueAndCommentDefault("lang.weather.warm.name", "&eТепло");
        addValueAndCommentDefault("lang.weather.warm.message", "&eПриятный тёплый ветерок обдувает вас. Сегодня %weather%");
        // Hot
        addComment("Hot");
        addValueAndCommentDefault("lang.weather.hot.name", "&eЖарко");
        addValueAndCommentDefault("lang.weather.hot.message", "&eНас как в печку поместили! Сегодня %weather%");
        // Scorching
        addComment("Scorching");
        addValueAndCommentDefault("lang.weather.scorching.name", "&eНевыносимо жарко");
        addValueAndCommentDefault("lang.weather.scorching.message", "&eСейчас на улице такое пекло, что кожа слазит. Сегодня %weather%");
        // Rainy
        addComment("Rainy");
        addValueAndCommentDefault("lang.weather.rainy.name", "&9Дождливо");
        addValueAndCommentDefault("lang.weather.rainy.message", "&3Вы лицезреете сильнейший ливень! Сегодня %weather%");
        // Chilly
        addComment("Chilly");
        addValueAndCommentDefault("lang.weather.chilly.name", "&9Прохладно");
        addValueAndCommentDefault("lang.weather.chilly.message", "&3Вы дрожите от холода! Сегодня %weather%");
        // Breezy
        addComment("Breezy");
        addValueAndCommentDefault("lang.weather.breezy.name", "&7Свежо");
        addValueAndCommentDefault("lang.weather.breezy.message", "&7Вас обдувает лёгкий ветерок. Сегодня %weather%");
        // Beautiful
        addComment("Beautiful");
        addValueAndCommentDefault("lang.weather.beautiful.name", "&aКрасиво");
        addValueAndCommentDefault("lang.weather.beautiful.message", "&aСолнце светит, жизнь прекрасна! Сегодня %weather%");

        /// EFFECTS
        addVoid();
        addComment("* EFFECTS");
        // Feels good
        addComment("Feels good");
        addValueAndCommentDefault("lang.effect.feelsGood.message.trigger", "");
        addValueAndCommentDefault("lang.effect.feelsGood.message.get", "");
        addValueAndCommentDefault("lang.effect.feelsGood.message.remove", "");
        // Fluffy Coat
        addComment("Fluffy Coat");
        addValueAndCommentDefault("lang.effect.fluffyCoat.message.trigger", "");
        addValueAndCommentDefault("lang.effect.fluffyCoat.message.get", "");
        addValueAndCommentDefault("lang.effect.fluffyCoat.message.remove", "");
        // Primitive Heating
        addComment("Primitive Heating");
        addValueAndCommentDefault("lang.effect.primitiveHeating.message.trigger", "");
        addValueAndCommentDefault("lang.effect.primitiveHeating.message.get", "");
        addValueAndCommentDefault("lang.effect.primitiveHeating.message.remove", "");
        // Revitalized
        addComment("Revitalized");
        addValueAndCommentDefault("lang.effect.revitalized.message.trigger", "");
        addValueAndCommentDefault("lang.effect.revitalized.message.get", "");
        addValueAndCommentDefault("lang.effect.revitalized.message.remove", "");
        // Warming Stew
        addComment("Warming Stew");
        addValueAndCommentDefault("lang.effect.warmingStew.message.trigger", "");
        addValueAndCommentDefault("lang.effect.warmingStew.message.get", "");
        addValueAndCommentDefault("lang.effect.warmingStew.message.remove", "");
        // Wind In Your Boots
        addComment("Wind In Your Boots");
        addValueAndCommentDefault("lang.effect.windInYourBoots.message.trigger", "");
        addValueAndCommentDefault("lang.effect.windInYourBoots.message.get", "");
        addValueAndCommentDefault("lang.effect.windInYourBoots.message.remove", "");
        // Devastation
        addComment("Devastation");
        addValueAndCommentDefault("lang.effect.devastation.message.trigger", "");
        addValueAndCommentDefault("lang.effect.devastation.message.get", "");
        addValueAndCommentDefault("lang.effect.devastation.message.remove", "");
        // Frostbite
        addComment("Frostbite");
        addValueAndCommentDefault("lang.effect.frostbite.message.trigger", "");
        addValueAndCommentDefault("lang.effect.frostbite.message.get", "");
        addValueAndCommentDefault("lang.effect.frostbite.message.remove", "");
        // Hold Onto Your Hat
        addComment("Hold Onto Your Hat");
        addValueAndCommentDefault("lang.effect.holdOntoYourHat.message.trigger", "");
        addValueAndCommentDefault("lang.effect.holdOntoYourHat.message.get", "");
        addValueAndCommentDefault("lang.effect.holdOntoYourHat.message.remove", "");
        // Hot Sand
        addComment("Hot Sand");
        addValueAndCommentDefault("lang.effect.hotSand.message.trigger", "");
        addValueAndCommentDefault("lang.effect.hotSand.message.get", "");
        addValueAndCommentDefault("lang.effect.hotSand.message.remove", "");
        // Icy
        addComment("Icy");
        addValueAndCommentDefault("lang.effect.icy.message.trigger", "");
        addValueAndCommentDefault("lang.effect.icy.message.get", "");
        addValueAndCommentDefault("lang.effect.icy.message.remove", "");
        // Soldering Iron
        addComment("Soldering Iron");
        addValueAndCommentDefault("lang.effect.solderingIron.message.trigger", "");
        addValueAndCommentDefault("lang.effect.solderingIron.message.get", "");
        addValueAndCommentDefault("lang.effect.solderingIron.message.remove", "");
        // Strong Current
        addComment("Strong Current");
        addValueAndCommentDefault("lang.effect.strongCurrent.message.trigger", "");
        addValueAndCommentDefault("lang.effect.strongCurrent.message.get", "");
        addValueAndCommentDefault("lang.effect.strongCurrent.message.remove", "");
        // Sweating
        addComment("Sweating");
        addValueAndCommentDefault("lang.effect.sweating.message.trigger", "");
        addValueAndCommentDefault("lang.effect.sweating.message.get", "");
        addValueAndCommentDefault("lang.effect.sweating.message.remove", "");
        // The Shivers
        addComment("The Shivers");
        addValueAndCommentDefault("lang.effect.theShivers.message.trigger", "");
        addValueAndCommentDefault("lang.effect.theShivers.message.get", "");
        addValueAndCommentDefault("lang.effect.theShivers.message.remove", "");
        // Wet Mud
        addComment("Wet Mud");
        addValueAndCommentDefault("lang.effect.wetMud.message.trigger", "");
        addValueAndCommentDefault("lang.effect.wetMud.message.get", "");
        addValueAndCommentDefault("lang.effect.wetMud.message.remove", "");
    }
}
