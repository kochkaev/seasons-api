package ru.kochkaev.api.seasons.config;

import com.google.gson.Gson;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class OldConfig {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;
    private static Gson gson = new Gson();
    private static File json;
    //private static FileOutputStream fos;
    //private static OutputStreamWriter osw;
    //private static JsonObject parser;
    //private static Reader reader;
    private static BufferedWriter writer;
    private static Map<String, String> current = new HashMap<>();

    //// CONF = Other configs
    //// LANG = Translation
    //// CURRENT = Current Weather/Season info (json)

    //// NAME = Display name of the Weather/Season
    //// MESSAGE = Message, sends to chat on trigger
    //// CHANCE =  chance of this weather coming on a new day (less than 100)

    /// SEASON
    // WINTER
    public static String LANG_SEASON_WINTER_NAME;
    public static String LANG_SEASON_WINTER_MESSAGE;
    // SPRING
    public static String LANG_SEASON_SPRING_NAME;
    public static String LANG_SEASON_SPRING_MESSAGE;
    // SUMMER
    public static String LANG_SEASON_SUMMER_NAME;
    public static String LANG_SEASON_SUMMER_MESSAGE;
    // FALL
    public static String LANG_SEASON_FALL_NAME;
    public static String LANG_SEASON_FALL_MESSAGE;

    /// WEATHER
    // NIGHT
    public static String LANG_WEATHER_NIGHT_NAME;
    public static String LANG_WEATHER_NIGHT_MESSAGE;
    // SNOWY
    public static String LANG_WEATHER_SNOWY_NAME;
    public static String LANG_WEATHER_SNOWY_MESSAGE;
    public static int CONF_WEATHER_SNOWY_CHANCE;
    // FREEZING
    public static String LANG_WEATHER_FREEZING_NAME;
    public static String LANG_WEATHER_FREEZING_MESSAGE;
    public static int CONF_WEATHER_FREEZING_CHANCE;
    // STORMY
    public static String LANG_WEATHER_STORMY_NAME;
    public static String LANG_WEATHER_STORMY_MESSAGE;
    public static int CONF_WEATHER_STORMY_CHANCE;
    // COLD
    public static String LANG_WEATHER_COLD_NAME;
    public static String LANG_WEATHER_COLD_MESSAGE;
    public static int CONF_WEATHER_COLD_CHANCE;
    // WARM
    public static String LANG_WEATHER_WARM_NAME;
    public static String LANG_WEATHER_WARM_MESSAGE;
    public static int CONF_WEATHER_WARM_CHANCE;
    // HOT
    public static String LANG_WEATHER_HOT_NAME;
    public static String LANG_WEATHER_HOT_MESSAGE;
    public static int CONF_WEATHER_HOT_CHANCE;
    // SCORCHING
    public static String LANG_WEATHER_SCORCHING_NAME;
    public static String LANG_WEATHER_SCORCHING_MESSAGE;
    public static int CONF_WEATHER_SCORCHING_CHANCE;
    // RAINY
    public static String LANG_WEATHER_RAINY_NAME;
    public static String LANG_WEATHER_RAINY_MESSAGE;
    public static int CONF_WEATHER_RAINY_CHANCE;
    // CHILLY
    public static String LANG_WEATHER_CHILLY_NAME;
    public static String LANG_WEATHER_CHILLY_MESSAGE;
    public static int CONF_WEATHER_CHILLY_CHANCE;
    // BREEZY
    public static String LANG_WEATHER_BREEZY_NAME;
    public static String LANG_WEATHER_BREEZY_MESSAGE;
    public static int CONF_WEATHER_BREEZY_CHANCE;
    // BEAUTIFUL
    public static String LANG_WEATHER_BEAUTIFUL_NAME;
    public static String LANG_WEATHER_BEAUTIFUL_MESSAGE;
    public static int CONF_WEATHER_BEAUTIFUL_CHANCE;

    /// CURRENT
    //public static Current current;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of("seasons-api/config").provider(configs).request();

        assignConfigs();
        openJson();
    }

    private static void createConfigs() {
        configs.addCommentLinePair("** CONF = Other configs");
        configs.addCommentLinePair("** LANG = Translation");
        configs.addCommentLinePair("** CURRENT = Current Weather/Season info");
        configs.addVoidPair();

        configs.addCommentLinePair("** NAME = Display name of the Weather/Season");
        configs.addCommentLinePair("** MESSAGE = Message, sends to chat on trigger");
        configs.addCommentLinePair("** CHANCE =  chance of this weather coming on a new day (less than 100)");
        configs.addVoidPair();

        /// SEASON
        configs.addVoidPair();
        configs.addCommentLinePair("* SEASON");
        // Winter
        configs.addCommentLinePair("Winter");
        configs.addKeyValuePair(new Pair<>("lang.season.winter.name", "&bЗима"), "String");
        configs.addKeyValuePair(new Pair<>("lang.season.winter.message", "&eДа свершится новогоднее чудо! Наступила %season%"), "String");
        // Spring
        configs.addCommentLinePair("Spring");
        configs.addKeyValuePair(new Pair<>("lang.season.spring.name", "&2Весна"), "String");
        configs.addKeyValuePair(new Pair<>("lang.season.spring.message", "&eС глаз долой весь снег! Наступила %season%"), "String");
        // Summer
        configs.addCommentLinePair("Summer");
        configs.addKeyValuePair(new Pair<>("lang.season.summer.name", "&aЛето"), "String");
        configs.addKeyValuePair(new Pair<>("lang.season.summer.message", "&eНастало время пекла! Наступило %season%"), "String");
        // Fall
        configs.addCommentLinePair("Fall");
        configs.addKeyValuePair(new Pair<>("lang.season.fall.name", "&6Осень"), "String");
        configs.addKeyValuePair(new Pair<>("lang.season.fall.message", "&eУнылая пора! Очей очарованье! Наступила %season%"), "String");

        /// WEATHER
        configs.addVoidPair();
        configs.addCommentLinePair("* WEATHER");
        // Night
        configs.addCommentLinePair("Night");
        configs.addKeyValuePair(new Pair<>("lang.weather.night.name", "&7Ночь"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.night.message", "&7Кажется темнеет... Наступает %weather%"), "String");
        // Snowy
        configs.addCommentLinePair("Snowy");
        configs.addKeyValuePair(new Pair<>("lang.weather.snowy.name", "&7Снежно"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.snowy.message", "&7Белый туман одеялом ложится на крыши и цветы. Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.snowy.chance", 15), "int");
        // Freezing
        configs.addCommentLinePair("Freezing");
        configs.addKeyValuePair(new Pair<>("lang.weather.freezing.name", "&9Морозно"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.freezing.message", "&3Вся вода замерзает! Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.freezing.chance", 15), "int");
        // Stormy
        configs.addCommentLinePair("Stormy");
        configs.addKeyValuePair(new Pair<>("lang.weather.stormy.name", "&cШторм"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.stormy.message", "&cГремит январская вьюга! Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.stormy.chance", 10), "int");
        // Cold
        configs.addCommentLinePair("Cold");
        configs.addKeyValuePair(new Pair<>("lang.weather.cold.name", "&9Холодно"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.cold.message", "&3На окнах появляется иней. Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.cold.chance", 40), "int");
        // Warm
        configs.addCommentLinePair("Warm");
        configs.addKeyValuePair(new Pair<>("lang.weather.warm.name", "&eТепло"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.warm.message", "&eПриятный тёплый ветерок обдувает вас. Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.warm.chance", 25), "int");
        // Hot
        configs.addCommentLinePair("Hot");
        configs.addKeyValuePair(new Pair<>("lang.weather.hot.name", "&eЖарко"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.hot.message", "&eНас как в печку поместили! Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.hot.chance", 20), "int");
        // Scorching
        configs.addCommentLinePair("Scorching");
        configs.addKeyValuePair(new Pair<>("lang.weather.scorching.name", "&eНевыносимо жарко"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.scorching.message", "&eСейчас на улице такое пекло, что кожа слазит. Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.scorching.chance", 10), "int");
        // Rainy
        configs.addCommentLinePair("Rainy");
        configs.addKeyValuePair(new Pair<>("lang.weather.rainy.name", "&9Дождливо"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.rainy.message", "&3Вы лицизреете сильнейший ливень! Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.rainy.chance", 10), "int");
        // Chilly
        configs.addCommentLinePair("Chilly");
        configs.addKeyValuePair(new Pair<>("lang.weather.chilly.name", "&9Прохладно"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.chilly.message", "&3Вы дрожите от холода! Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.chilly.chance", 15), "int");
        // Breezy
        configs.addCommentLinePair("Breezy");
        configs.addKeyValuePair(new Pair<>("lang.weather.breezy.name", "&7Свежо"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.breezy.message", "&7Вас обдувает лёгкий ветерок. Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.breezy.chance", 15), "int");
        // Beautiful
        configs.addCommentLinePair("Beautiful");
        configs.addKeyValuePair(new Pair<>("lang.weather.beautiful.name", "&aКрасиво"), "String");
        configs.addKeyValuePair(new Pair<>("lang.weather.beautiful.message", "&aСолнце светит, жизнь прекрасна! Сегодня %weather%"), "String");
        configs.addKeyValuePair(new Pair<>("conf.weather.beautiful.chance", 20), "int");
    }

    private static void assignConfigs() {
        /// SEASON
        // WINTER
        LANG_SEASON_WINTER_NAME = CONFIG.getOrDefault("lang.season.winter.name", "&bЗима");
        LANG_SEASON_WINTER_MESSAGE = CONFIG.getOrDefault("lang.season.winter.message", "&eДа свершится новогоднее чудо! Наступила %season%");
        // SPRING
        LANG_SEASON_SPRING_NAME = CONFIG.getOrDefault("lang.season.spring.name", "&2Весна");
        LANG_SEASON_SPRING_MESSAGE = CONFIG.getOrDefault("lang.season.spring.message", "&eС глаз долой весь снег! Наступила %season%");
        // SUMMER
        LANG_SEASON_SUMMER_NAME = CONFIG.getOrDefault("lang.season.summer.name", "&aЛето");
        LANG_SEASON_SUMMER_MESSAGE = CONFIG.getOrDefault("lang.season.summer.message", "&eНастало время пекла! Наступило %season%");
        // FALL
        LANG_SEASON_FALL_NAME = CONFIG.getOrDefault("lang.season.fall.name", "&6Осень");
        LANG_SEASON_FALL_MESSAGE = CONFIG.getOrDefault("lang.season.fall.message", "&eУнылая пора! Очей очарованье! Наступила %season%");

        /// WEATHER
        // NIGHT
        LANG_WEATHER_NIGHT_NAME = CONFIG.getOrDefault("lang.weather.night.name", "&7Ночь");
        LANG_WEATHER_NIGHT_MESSAGE = CONFIG.getOrDefault("lang.weather.night.message", "&7Кажется темнеет... Наступает %weather%");
        // SNOWY
        LANG_WEATHER_SNOWY_NAME = CONFIG.getOrDefault("lang.weather.snowy.name", "&7Снежно");
        LANG_WEATHER_SNOWY_MESSAGE = CONFIG.getOrDefault("lang.weather.snowy.message", "&7Белый туман одеялом ложится на крыши и цветы. Сегодня %weather%");
        CONF_WEATHER_SNOWY_CHANCE = CONFIG.getOrDefault("conf.weather.snowy.chance", 15);
        // FREEZING
        LANG_WEATHER_FREEZING_NAME = CONFIG.getOrDefault("lang.weather.freezing.name", "&9Морозно");
        LANG_WEATHER_FREEZING_MESSAGE = CONFIG.getOrDefault("lang.weather.freezing.message", "&3Вся вода замерзает! Сегодня %weather%");
        CONF_WEATHER_FREEZING_CHANCE = CONFIG.getOrDefault("conf.weather.freezing.chance", 15);
        // STORMY
        LANG_WEATHER_STORMY_NAME = CONFIG.getOrDefault("lang.weather.stormy.name", "&cШторм");
        LANG_WEATHER_STORMY_MESSAGE = CONFIG.getOrDefault("lang.weather.stormy.message", "&cГремит январская вьюга! Сегодня %weather%");
        CONF_WEATHER_STORMY_CHANCE = CONFIG.getOrDefault("conf.weather.stormy.chance", 10);
        // COLD
        LANG_WEATHER_COLD_NAME = CONFIG.getOrDefault("lang.weather.cold.name", "&9Холодно");
        LANG_WEATHER_COLD_MESSAGE = CONFIG.getOrDefault("lang.weather.cold.message", "&3На окнах появляется иней. Сегодня %weather%");
        CONF_WEATHER_COLD_CHANCE = CONFIG.getOrDefault("conf.weather.cold.chance", 40);
        // WARM
        LANG_WEATHER_WARM_NAME = CONFIG.getOrDefault("lang.weather.warm.name", "&eТепло");
        LANG_WEATHER_WARM_MESSAGE = CONFIG.getOrDefault("lang.weather.warm.message", "&eПриятный тёплый ветерок обдувает вас. Сегодня %weather%");
        CONF_WEATHER_WARM_CHANCE = CONFIG.getOrDefault("conf.weather.warm.chance", 25);
        // HOT
        LANG_WEATHER_HOT_NAME = CONFIG.getOrDefault("lang.weather.hot.name", "&eЖарко");
        LANG_WEATHER_HOT_MESSAGE = CONFIG.getOrDefault("lang.weather.hot.message", "&eНас как в печку поместили! Сегодня %weather%");
        CONF_WEATHER_HOT_CHANCE = CONFIG.getOrDefault("conf.weather.hot.chance", 20);
        // SCORCHING
        LANG_WEATHER_SCORCHING_NAME = CONFIG.getOrDefault("lang.weather.scorching.name", "&eНевыносимо жарко");
        LANG_WEATHER_SCORCHING_MESSAGE = CONFIG.getOrDefault("lang.weather.scorching.message", "&eСейчас на улице такое пекло, что кожа слазит. Сегодня %weather%");
        CONF_WEATHER_SCORCHING_CHANCE = CONFIG.getOrDefault("conf.weather.scorching.chance", 10);
        // RAINY
        LANG_WEATHER_RAINY_NAME = CONFIG.getOrDefault("lang.weather.rainy.name", "&9Дождливо");
        LANG_WEATHER_RAINY_MESSAGE = CONFIG.getOrDefault("lang.weather.rainy.message", "&3Вы лицизреете сильнейший ливень! Сегодня %weather%");
        CONF_WEATHER_RAINY_CHANCE = CONFIG.getOrDefault("conf.weather.rainy.chance", 10);
        // CHILLY
        LANG_WEATHER_CHILLY_NAME = CONFIG.getOrDefault("lang.weather.chilly.name", "&9Прохладно");
        LANG_WEATHER_CHILLY_MESSAGE = CONFIG.getOrDefault("lang.weather.chilly.message", "&3Вы дрожите от холода! Сегодня %weather%");
        CONF_WEATHER_CHILLY_CHANCE = CONFIG.getOrDefault("conf.weather.chilly.chance", 15);
        // BREEZY
        LANG_WEATHER_BREEZY_NAME = CONFIG.getOrDefault("lang.weather.breezy.name", "&7Свежо");
        LANG_WEATHER_BREEZY_MESSAGE = CONFIG.getOrDefault("lang.weather.breezy.message", "&7Вас обдувает лёгкий ветерок. Сегодня %weather%");
        CONF_WEATHER_BREEZY_CHANCE = CONFIG.getOrDefault("conf.weather.breezy.chance", 15);
        // BEAUTIFUL
        LANG_WEATHER_BEAUTIFUL_NAME = CONFIG.getOrDefault("lang.weather.beautiful.name", "&aКрасиво");
        LANG_WEATHER_BEAUTIFUL_MESSAGE = CONFIG.getOrDefault("lang.weather.beautiful.message", "&aСолнце светит, жизнь прекрасна! Сегодня %weather%");
        CONF_WEATHER_BEAUTIFUL_CHANCE = CONFIG.getOrDefault("conf.weather.beautiful.chance", 20);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
    private static void openJson(){
        String pathStr = FabricLoader.getInstance().getConfigDir().resolve("seasons-api/current.json").toString();
        json = new File(pathStr);
        try {
            if (!json.exists()) {
                json.createNewFile();
                writer = Files.newBufferedWriter(Paths.get(pathStr));
                current.put("season", "WINTER");
                current.put("weather", "NIGHT");
                //fos = new FileOutputStream(json);
                //osw = new OutputStreamWriter(fos);
                //String jsonDefStr = "[ { \"season\": \"WINTER\", \"weather\": \"NIGHT\" } ]";
                //osw.write(jsonDefStr);

            }
            else {
                writer = Files.newBufferedWriter(Paths.get(pathStr));
                //fos = new FileOutputStream(json);
                //osw = new OutputStreamWriter(fos);

            }

            //reader = Files.newBufferedReader(Paths.get(patchStr));
            //parser = JsonParser.parseReader(reader).getAsJsonObject();
            //current = gson.fromJson(fos.toString(), Current.class);
            //return current;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void closeJson(){
        String jsonStr = gson.toJson(current);
        try {
            writer.write(gson.toJson(current));
            writer.close();
            //reader.close();
            //osw.write(jsonStr);
            //osw.close();
            //fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //public class Current{
    //    public static String season;
    //    public static String weather;
    //}

    public static String getCurrentWeather() { return current.get("weather"); }
    public static void setCurrentWeather(String value) { current.put("weather", value); }
    public static String getCurrentSeason() { return current.get("season"); }
    public static void setCurrentSeason(String value) { current.put("season", value); }
}
