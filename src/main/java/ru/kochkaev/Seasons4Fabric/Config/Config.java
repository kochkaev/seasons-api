package ru.kochkaev.Seasons4Fabric.Config;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private static Map<String, String> config = new HashMap<>();
    private static JsonObject current = new JsonObject();
    private static JSONConfigCoreTools jsonCore;

    public static void init__() {

        String defaultConfig = DefaultTXTConfig.init__();
        config = TXTConfigCore.openOrCreate("Seasons4Fabric/config", defaultConfig);
        String defaultCurrent = "{ \"season\": \"WINTER\", \"weather\": \"NIGHT\" }";
        jsonCore = JSONConfigCore.openOrCreate("Seasons4Fabric/current", defaultCurrent);
        current = jsonCore.getJsonObject();

    }

    public static void saveCurrent() {
        jsonCore.writeJsonObject(current);
        jsonCore.save();
    }

    public static String getString(String key) {
        return config.get(key);
    }
    public static int getInt(String key) {
        return Integer.parseInt(config.get(key));
    }
    public static long getLong(String key) {
        return Long.parseLong(config.get(key));
    }

    public static String getCurrent(String key) { return current.get(key).getAsString(); }
    public static void writeCurrent(String key, String value) { current.addProperty(key, value); }

}

class DefaultTXTConfig {

    private static TXTConfigCore.GenerateDefaults gen = new TXTConfigCore.GenerateDefaults();

    public static String init__() {

        generate();
        String defaultConfig = gen.getGenerated();
        return defaultConfig;

    }

    private static void generate() {

        String copyright = "# ------------------------------------------------\n# Seasons4Fabric\n# ------------------------------------------------\n" +
                "# This mod was developed by analogy with the Spigot plugin \"Seasons\"\n# specifically for the private Minecraft server \"Zixa City\"\n# by its administrator (kochkaev, aka kleverdi).\n# The idea of this mod was taken from Harieo.\n" +
                "# ------------------------------------------------\n# Harieo on GitHub: https://github.com/Harieo/\n# Original plugin on GitHub: https://github.com/Harieo/Seasons/\n# Original plugin on SpigotMC: https://www.spigotmc.org/resources/seasons.39298/\n" +
                "# ------------------------------------------------\n# Created by @kochkaev\n#   - GitHub: https://github.com/kochkaev/\n#   - VK: https://vk.com/kleverdi/\n#   - YouTube: https://youtube.com/@kochkaev/\n#   - Contact email: kleverdi@vk.com\n"+
                "# ------------------------------------------------\n# WARN: It's server-side mod.\n# ------------------------------------------------\n# # # # # # # # # # # # # # # # # # # # # # # # # #\n#\n# It's mod config!\n#\n# ** LANG = Translation\n" +
                "# ** CONF = Other configs\n#\n# ** NAME = Display name of the Weather/Season\n# ** MESSAGE = Message, sends to chat on trigger\n# ** CHANCE =  chance of this weather coming on a new day (less than 100)\n#\n# Placeholders:\n#   - %season% - replaces to current season.\n" +
                "#   - %weather% - replaces to current weather.\n#   - & - replaces to \"§\" (colour codes).\n#\n# Other config files:\n#   - current.json - information about the current Weather/Season\n#     is updated when the server is turned off.\n\n\n";
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
        gen.addValueAndCommentDefault("conf.weather.snowy.chance", "15");
        // Freezing
        gen.addComment("Freezing");
        gen.addValueAndCommentDefault("lang.weather.freezing.name", "&9Морозно");
        gen.addValueAndCommentDefault("lang.weather.freezing.message", "&3Вся вода замерзает! Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.freezing.chance", "15");
        // Stormy
        gen.addComment("Stormy");
        gen.addValueAndCommentDefault("lang.weather.stormy.name", "&cШторм");
        gen.addValueAndCommentDefault("lang.weather.stormy.message", "&cГремит январская вьюга! Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.stormy.chance", "10");
        // Cold
        gen.addComment("Cold");
        gen.addValueAndCommentDefault("lang.weather.cold.name", "&9Холодно");
        gen.addValueAndCommentDefault("lang.weather.cold.message", "&3На окнах появляется иней. Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.cold.chance", "40");
        // Warm
        gen.addComment("Warm");
        gen.addValueAndCommentDefault("lang.weather.warm.name", "&eТепло");
        gen.addValueAndCommentDefault("lang.weather.warm.message", "&eПриятный тёплый ветерок обдувает вас. Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.warm.chance", "25");
        // Hot
        gen.addComment("Hot");
        gen.addValueAndCommentDefault("lang.weather.hot.name", "&eЖарко");
        gen.addValueAndCommentDefault("lang.weather.hot.message", "&eНас как в печку поместили! Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.hot.chance", "20");
        // Scorching
        gen.addComment("Scorching");
        gen.addValueAndCommentDefault("lang.weather.scorching.name", "&eНевыносимо жарко");
        gen.addValueAndCommentDefault("lang.weather.scorching.message", "&eСейчас на улице такое пекло, что кожа слазит. Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.scorching.chance", "10");
        // Rainy
        gen.addComment("Rainy");
        gen.addValueAndCommentDefault("lang.weather.rainy.name", "&9Дождливо");
        gen.addValueAndCommentDefault("lang.weather.rainy.message", "&3Вы лицезреете сильнейший ливень! Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.rainy.chance", "10");
        // Chilly
        gen.addComment("Chilly");
        gen.addValueAndCommentDefault("lang.weather.chilly.name", "&9Прохладно");
        gen.addValueAndCommentDefault("lang.weather.chilly.message", "&3Вы дрожите от холода! Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.chilly.chance", "15");
        // Breezy
        gen.addComment("Breezy");
        gen.addValueAndCommentDefault("lang.weather.breezy.name", "&7Свежо");
        gen.addValueAndCommentDefault("lang.weather.breezy.message", "&7Вас обдувает лёгкий ветерок. Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.breezy.chance", "15");
        // Beautiful
        gen.addComment("Beautiful");
        gen.addValueAndCommentDefault("lang.weather.beautiful.name", "&aКрасиво");
        gen.addValueAndCommentDefault("lang.weather.beautiful.message", "&aСолнце светит, жизнь прекрасна! Сегодня %weather%");
        gen.addValueAndCommentDefault("conf.weather.beautiful.chance", "20");

        // Settinge
        gen.addVoid();
        gen.addComment("* SETTINGS");
        gen.addComment("Tick of day starts");
        gen.addValueAndCommentDefault("conf.tick.day.start", "0");
        gen.addComment("Tick of day ends");
        gen.addValueAndCommentDefault("conf.tick.day.end", "12542");

    }

}

//  This classes has been created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com