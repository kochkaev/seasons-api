package ru.kochkaev.Seasons4Fabric.Config;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private static ConfigObject config;
    private static ConfigObject lang;
    private static JsonObject current = new JsonObject();
    private static JSONConfigCoreTools jsonCore;

    public static void init__() {

        String defaultConfig = DefaultTXTConfig.init__();
        config = new ConfigObject(TXTConfigCore.openOrCreate("Seasons4Fabric/config", defaultConfig));
        String defaultCurrent = "{ \"season\": \"WINTER\", \"weather\": \"NIGHT\", \"language\": \"RU_ru\" }";
        jsonCore = JSONConfigCore.openOrCreate("Seasons4Fabric/current", defaultCurrent);
        current = jsonCore.getJsonObject();
        TXTConfigCore.createIfDoNotExists("Seasons4Fabric/lang/RU_ru", DefaultTXTLangRU.init__());
        lang = new ConfigObject(TXTConfigCore.open("Seasons4Fabric/lang/"+current.get("language")));

    }

    public static void reload() {
        config.reload();
    }

    public static void saveCurrent() {
        jsonCore.writeJsonObject(current);
        jsonCore.save();
    }

    public static String getCurrent(String key) { return current.get(key).getAsString(); }
    public static void writeCurrent(String key, String value) { current.addProperty(key, value); }

    public static ConfigObject getLang() { return lang; }
    public static ConfigObject getConfig() { return config; }

    public static String getCopyright() {
        return "# ------------------------------------------------\n# Seasons4Fabric\n# ------------------------------------------------\n" +
                "# This mod was developed by analogy with the Spigot plugin \"Seasons\"\n# specifically for the private Minecraft server \"Zixa City\"\n# by its administrator (kochkaev, aka kleverdi).\n# The idea of this mod was taken from Harieo.\n" +
                "# ------------------------------------------------\n# Harieo on GitHub: https://github.com/Harieo/\n# Original plugin on GitHub: https://github.com/Harieo/Seasons/\n# Original plugin on SpigotMC: https://www.spigotmc.org/resources/seasons.39298/\n" +
                "# ------------------------------------------------\n# Created by @kochkaev\n#   - GitHub: https://github.com/kochkaev/\n#   - VK: https://vk.com/kleverdi/\n#   - YouTube: https://youtube.com/@kochkaev/\n#   - Contact email: kleverdi@vk.com\n"+
                "# ------------------------------------------------\n# WARN: It's server-side mod.\n# ------------------------------------------------\n# # # # # # # # # # # # # # # # # # # # # # # # # #\n";
    }
}

class DefaultTXTConfig {

    private final static TXTConfigCore.GenerateDefaults gen = new TXTConfigCore.GenerateDefaults();

    public static String init__() {

        generate();
        String defaultConfig = gen.getGenerated();
        return defaultConfig;

    }

    private static void generate() {

        String copyright = Config.getCopyright() + "#\n# It's mod config!\n#" +
                "\n# ** CHANCE =  chance of this weather coming on a new day (less than 100)\n#" +
                "\n# Other config files:\n#   - current.json - information about the current Weather/Season,\n#     is updated when the server is turned off.\n#   - lang - directory, contains translations of mod names/messages.\n\n\n";
        gen.addString(copyright);

        /// Chances of weather
        gen.addVoid();
        gen.addComment("* WEATHER CHANCE");
        // Snowy
        gen.addComment("Snowy");
        gen.addValueAndCommentDefault("conf.weather.snowy.chance", "15");
        // Freezing
        gen.addComment("Freezing");
        gen.addValueAndCommentDefault("conf.weather.freezing.chance", "15");
        // Stormy
        gen.addComment("Stormy");
        gen.addValueAndCommentDefault("conf.weather.stormy.chance", "10");
        // Cold
        gen.addComment("Cold");
        gen.addValueAndCommentDefault("conf.weather.cold.chance", "40");
        // Warm
        gen.addComment("Warm");
        gen.addValueAndCommentDefault("conf.weather.warm.chance", "25");
        // Hot
        gen.addComment("Hot");
        gen.addValueAndCommentDefault("conf.weather.hot.chance", "20");
        // Scorching
        gen.addComment("Scorching");
        gen.addValueAndCommentDefault("conf.weather.scorching.chance", "10");
        // Rainy
        gen.addComment("Rainy");
        gen.addValueAndCommentDefault("conf.weather.rainy.chance", "10");
        // Chilly
        gen.addComment("Chilly");
        gen.addValueAndCommentDefault("conf.weather.chilly.chance", "15");
        // Breezy
        gen.addComment("Breezy");
        gen.addValueAndCommentDefault("conf.weather.breezy.chance", "15");
        // Beautiful
        gen.addComment("Beautiful");
        gen.addValueAndCommentDefault("conf.weather.beautiful.chance", "20");

        // Settings
        gen.addVoid();
        gen.addComment("* SETTINGS");
        gen.addComment("Tick of day starts");
        gen.addValueAndCommentDefault("conf.tick.day.start", "0");
        gen.addComment("Tick of day ends");
        gen.addValueAndCommentDefault("conf.tick.day.end", "12542");
        gen.addComment("Seconds per seasons tick");
        gen.addValueAndCommentDefault("conf.tick.secondsPerTick", "1");
        gen.addComment("Ticks before action starts (for example, damage)");
        gen.addValueAndCommentDefault("conf.tick.ticksPerAction", "3");

    }

}

class DefaultTXTLangRU {

    private final static TXTConfigCore.GenerateDefaults gen = new TXTConfigCore.GenerateDefaults();

    public static String init__() {

        generate();
        String defaultLang = gen.getGenerated();
        return defaultLang;

    }

    private static void generate() {

        String copyright = Config.getCopyright() + "#\n# It's mod lang!\n#" +
        "\n# ** NAME = Display name of the Weather/Season\n# ** MESSAGE = Message, sends to chat on trigger\n#\n# Placeholders:\n#   - %season% - replaces to current season.\n" +
                "#   - %weather% - replaces to current weather.\n#   - & - replaces to \"§\" (colour codes).\n#\n# Other config files:\n#   - current.json - information about the current Weather/Season,\n#     is updated when the server is turned off.\n#   - lang - directory, contains translations of mod names/messages.\n\n\n";
        gen.addString(copyright);

        /// SEASON
        gen.addComment("* SEASON");
        // Winter
        gen.addComment("Winter");
        gen.addValueAndCommentDefault("lang.season.winter.name", "&bЗима");
        gen.addValueAndCommentDefault("lang.season.winter.message", "&eДа свершится новогоднее чудо! Наступила %season%");
        // Spring
        gen.addComment("Spring");
        gen.addValueAndCommentDefault("lang.season.spring.name", "&2Весна");
        gen.addValueAndCommentDefault("lang.season.spring.message", "&eС глаз долой весь снег! Наступила %season%");
        // Summer
        gen.addComment("Summer");
        gen.addValueAndCommentDefault("lang.season.summer.name", "&aЛето");
        gen.addValueAndCommentDefault("lang.season.summer.message", "&eНастало время пекла! Наступило %season%");
        // Fall
        gen.addComment("Fall");
        gen.addValueAndCommentDefault("lang.season.fall.name", "&6Осень");
        gen.addValueAndCommentDefault("lang.season.fall.message", "&eУнылая пора! Очей очарованье! Наступила %season%");

        /// WEATHER
        gen.addVoid();
        gen.addComment("* WEATHER");
        // Night
        gen.addComment("Night");
        gen.addValueAndCommentDefault("lang.weather.night.name", "&7Ночь");
        gen.addValueAndCommentDefault("lang.weather.night.message", "&7Кажется темнеет... Наступает %weather%");
        // Snowy
        gen.addComment("Snowy");
        gen.addValueAndCommentDefault("lang.weather.snowy.name", "&7Снежно");
        gen.addValueAndCommentDefault("lang.weather.snowy.message", "&7Белый туман одеялом ложится на крыши и цветы. Сегодня %weather%");
        // Freezing
        gen.addComment("Freezing");
        gen.addValueAndCommentDefault("lang.weather.freezing.name", "&9Морозно");
        gen.addValueAndCommentDefault("lang.weather.freezing.message", "&3Вся вода замерзает! Сегодня %weather%");
        // Stormy
        gen.addComment("Stormy");
        gen.addValueAndCommentDefault("lang.weather.stormy.name", "&cШторм");
        gen.addValueAndCommentDefault("lang.weather.stormy.message", "&cГремит январская вьюга! Сегодня %weather%");
        // Cold
        gen.addComment("Cold");
        gen.addValueAndCommentDefault("lang.weather.cold.name", "&9Холодно");
        gen.addValueAndCommentDefault("lang.weather.cold.message", "&3На окнах появляется иней. Сегодня %weather%");
        // Warm
        gen.addComment("Warm");
        gen.addValueAndCommentDefault("lang.weather.warm.name", "&eТепло");
        gen.addValueAndCommentDefault("lang.weather.warm.message", "&eПриятный тёплый ветерок обдувает вас. Сегодня %weather%");
        // Hot
        gen.addComment("Hot");
        gen.addValueAndCommentDefault("lang.weather.hot.name", "&eЖарко");
        gen.addValueAndCommentDefault("lang.weather.hot.message", "&eНас как в печку поместили! Сегодня %weather%");
        // Scorching
        gen.addComment("Scorching");
        gen.addValueAndCommentDefault("lang.weather.scorching.name", "&eНевыносимо жарко");
        gen.addValueAndCommentDefault("lang.weather.scorching.message", "&eСейчас на улице такое пекло, что кожа слазит. Сегодня %weather%");
        // Rainy
        gen.addComment("Rainy");
        gen.addValueAndCommentDefault("lang.weather.rainy.name", "&9Дождливо");
        gen.addValueAndCommentDefault("lang.weather.rainy.message", "&3Вы лицезреете сильнейший ливень! Сегодня %weather%");
        // Chilly
        gen.addComment("Chilly");
        gen.addValueAndCommentDefault("lang.weather.chilly.name", "&9Прохладно");
        gen.addValueAndCommentDefault("lang.weather.chilly.message", "&3Вы дрожите от холода! Сегодня %weather%");
        // Breezy
        gen.addComment("Breezy");
        gen.addValueAndCommentDefault("lang.weather.breezy.name", "&7Свежо");
        gen.addValueAndCommentDefault("lang.weather.breezy.message", "&7Вас обдувает лёгкий ветерок. Сегодня %weather%");
        // Beautiful
        gen.addComment("Beautiful");
        gen.addValueAndCommentDefault("lang.weather.beautiful.name", "&aКрасиво");
        gen.addValueAndCommentDefault("lang.weather.beautiful.message", "&aСолнце светит, жизнь прекрасна! Сегодня %weather%");

        /// EFFECTS
        gen.addVoid();
        gen.addComment("* EFFECTS");
        // Feels good
        gen.addComment("Feels good");
        gen.addValueAndCommentDefault("lang.effect.feelsGood.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.feelsGood.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.feelsGood.message.remove", "");
        // Fluffy Coat
        gen.addComment("Fluffy Coat");
        gen.addValueAndCommentDefault("lang.effect.fluffyCoat.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.fluffyCoat.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.fluffyCoat.message.remove", "");
        // Primitive Heating
        gen.addComment("Primitive Heating");
        gen.addValueAndCommentDefault("lang.effect.primitiveHeating.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.primitiveHeating.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.primitiveHeating.message.remove", "");
        // Revitalized
        gen.addComment("Revitalized");
        gen.addValueAndCommentDefault("lang.effect.revitalized.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.revitalized.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.revitalized.message.remove", "");
        // Warming Stew
        gen.addComment("Warming Stew");
        gen.addValueAndCommentDefault("lang.effect.warmingStew.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.warmingStew.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.warmingStew.message.remove", "");
        // Wind In Your Boots
        gen.addComment("Wind In Your Boots");
        gen.addValueAndCommentDefault("lang.effect.windInYourBoots.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.windInYourBoots.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.windInYourBoots.message.remove", "");
        // Devastation
        gen.addComment("Devastation");
        gen.addValueAndCommentDefault("lang.effect.devastation.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.devastation.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.devastation.message.remove", "");
        // Frostbite
        gen.addComment("Frostbite");
        gen.addValueAndCommentDefault("lang.effect.frostbite.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.frostbite.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.frostbite.message.remove", "");
        // Hold Onto Your Hat
        gen.addComment("Hold Onto Your Hat");
        gen.addValueAndCommentDefault("lang.effect.holdOntoYourHat.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.holdOntoYourHat.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.holdOntoYourHat.message.remove", "");
        // Hot Sand
        gen.addComment("Hot Sand");
        gen.addValueAndCommentDefault("lang.effect.hotSand.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.hotSand.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.hotSand.message.remove", "");
        // Icy
        gen.addComment("Icy");
        gen.addValueAndCommentDefault("lang.effect.icy.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.icy.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.icy.message.remove", "");
        // Soldering Iron
        gen.addComment("Soldering Iron");
        gen.addValueAndCommentDefault("lang.effect.solderingIron.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.solderingIron.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.solderingIron.message.remove", "");
        // Strong Current
        gen.addComment("Strong Current");
        gen.addValueAndCommentDefault("lang.effect.strongCurrent.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.strongCurrent.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.strongCurrent.message.remove", "");
        // Sweating
        gen.addComment("Sweating");
        gen.addValueAndCommentDefault("lang.effect.sweating.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.sweating.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.sweating.message.remove", "");
        // The Shivers
        gen.addComment("The Shivers");
        gen.addValueAndCommentDefault("lang.effect.theShivers.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.theShivers.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.theShivers.message.remove", "");
        // Wet Mud
        gen.addComment("Wet Mud");
        gen.addValueAndCommentDefault("lang.effect.wetMud.message.trigger", "");
        gen.addValueAndCommentDefault("lang.effect.wetMud.message.get", "");
        gen.addValueAndCommentDefault("lang.effect.wetMud.message.remove", "");
    }

}

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com