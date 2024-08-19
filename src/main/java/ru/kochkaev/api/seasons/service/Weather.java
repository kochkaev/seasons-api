package ru.kochkaev.api.seasons.service;

//import ru.kochkaev.seasons-api.Config.OldConfig;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import ru.kochkaev.api.seasons.ChallengesTicker;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.util.Title;

import java.util.*;

public class Weather {

    private static final Random random = new Random();
    private static ServerWorld world;

//    private static WeatherObject CURRENT_WEATHER = new ExampleWeather();
    private static WeatherObject CURRENT_WEATHER;
    private static WeatherObject CURRENT_WEATHER_PREVIOUS;
    private static final List<WeatherObject> dailyWeathers = new ArrayList<>();
    private static final List<WeatherObject> nightlyWeathers = new ArrayList<>();
    private static final Map<String, WeatherObject> allWeathers = new HashMap<>();
    private static @Nullable Boolean isNight;

    public static void register(WeatherObject weather) {
        allWeathers.put(weather.getId(), weather);
        @Nullable
        Boolean nightly = weather.isNightly();
        if (nightly != null) {
            if (nightly) nightlyWeathers.add(weather);
            else dailyWeathers.add(weather);
        }
    }

    public static WeatherObject getChancedWeather(List<WeatherObject> weathers){
        Map<WeatherObject, Integer> chances = new HashMap<>();
        int maxChance = 0;
        for (WeatherObject weather : weathers){
            int chance = weather.getChance();
            if (weather.isEnabled() && chance > 0){
                chances.put(weather, chance);
                maxChance += chance;
            }
        }
        int randInt = (int) (random.nextFloat() * (maxChance - 1) + 1);
        for (WeatherObject weather : chances.keySet()){
            if (maxChance - randInt <= chances.get(weather)) { return weather; }
            else maxChance -= chances.get(weather);
        }
        return getWeatherByID("example");
    }

    public static List<WeatherObject> getSeasonDailyWeathers(SeasonObject season){
        List<WeatherObject> weathers = new ArrayList<>();
        for (WeatherObject weather : dailyWeathers){
            if (weather.getSeasons() != null) {
                assert weather.getSeasons() != null;
                if ((weather.getSeasons().contains(season)) && (weather.getChance() > 0)) {
                    weathers.add(weather);
                }
            }
        }
        return weathers;
    }
    public static List<WeatherObject> getSeasonNightlyWeathers(SeasonObject season){
        List<WeatherObject> weathers = new ArrayList<>();
        for (WeatherObject weather : nightlyWeathers){
            if (weather.getSeasons() != null) {
                assert weather.getSeasons() != null;
                if ((weather.getSeasons().contains(season)) && (weather.getChance() > 0)) {
                    weathers.add(weather);
                }
            }
        }
        return weathers;
    }

    public static WeatherObject getCurrent(){
        return CURRENT_WEATHER;
    }
    public static WeatherObject getPreviousCurrent(){
        return CURRENT_WEATHER_PREVIOUS;
    }

    public static void setCurrent(WeatherObject weather){
        CURRENT_WEATHER_PREVIOUS = CURRENT_WEATHER;
        CURRENT_WEATHER = weather;
        if (CURRENT_WEATHER.isNightly() == CURRENT_WEATHER_PREVIOUS.isNightly()) {
            SeasonObject season = Season.getCurrent();
            CURRENT_WEATHER_PREVIOUS = getChancedWeather(Boolean.TRUE.equals(CURRENT_WEATHER.isNightly()) ? getSeasonDailyWeathers(season) : getSeasonNightlyWeathers(season));
        }
        saveCurrentToConfig();
    }

    public static void onServerStartup(){
        world = SeasonsAPI.getOverworld();
        String currentStr = Config.getCurrent("weather");
        String prevCurrentStr = Config.getCurrent("previous-weather");
        if (currentStr.equals("NONE") || currentStr.equals("example")) {
            boolean isDay = world.getTimeOfDay() % 24000L < Config.getModConfig("API").getConfig().getLong("conf.tick.day.end");
            CURRENT_WEATHER = (prevCurrentStr.equals("NONE")) ? getChancedWeather(isDay ? getSeasonDailyWeathers(Season.getCurrent()) : getSeasonNightlyWeathers(Season.getCurrent())) : getWeatherByID(prevCurrentStr);
            if (isDay) {
                setDay();
            }
            else setNight();
        }
        else {
            CURRENT_WEATHER = getWeatherByID(currentStr);
        }
        if (prevCurrentStr.equals("NONE") || !prevCurrentStr.equals(currentStr)) CURRENT_WEATHER_PREVIOUS = getWeatherByID(prevCurrentStr);
        else {
            SeasonObject season = Season.getCurrent();
            CURRENT_WEATHER_PREVIOUS = getChancedWeather(Boolean.TRUE.equals(Objects.requireNonNull(Weather.getWeatherByID(currentStr)).isNightly()) ? getSeasonDailyWeathers(season) : getSeasonNightlyWeathers(season));
        }
        isNight = Boolean.TRUE.equals(CURRENT_WEATHER.isNightly());
    }

    public static void saveCurrentToConfig(){
        String currentStr = CURRENT_WEATHER.getId();
        String prevCurrentStr = CURRENT_WEATHER_PREVIOUS.getId();
        Config.writeCurrent("weather", currentStr);
        Config.writeCurrent("previous-weather", prevCurrentStr);
        Config.saveCurrent();
    }

    public static void reloadFromConfig() {
        String currentStr = Config.getCurrent("weather");
        String prevCurrentStr = Config.getCurrent("previous-weather");
        WeatherObject curWeather = getWeatherByID(currentStr);
        WeatherObject curWeatherPrev = getWeatherByID(prevCurrentStr);
        if (CURRENT_WEATHER != curWeather) {
            setWeather(curWeather);
        }
        if (CURRENT_WEATHER_PREVIOUS != curWeatherPrev) {
            CURRENT_WEATHER_PREVIOUS = curWeatherPrev;
        }
        SeasonsAPI.getLogger().info("Weathers was reloaded!");
    }

    public static void reloadDynamics() {
        for (WeatherObject weather : allWeathers.values()) {
            weather.onReload();
        }
    }

    public static void setChancedWeatherInCurrentSeason(){
        SeasonObject currentSeason = Season.getCurrent();
        WeatherObject weather = getChancedWeather(Boolean.TRUE.equals(isNight) ? getSeasonNightlyWeathers(currentSeason) : getSeasonDailyWeathers(currentSeason));
        setWeather(weather);
    }

    public static @Nullable Boolean isNight() { return isNight; }
    public static void setIsNight(@Nullable Boolean night) { isNight = night; }

    public static void setWeather(WeatherObject weather) {
        CURRENT_WEATHER.onWeatherRemove();
        Weather.setCurrent(weather);
        weather.onWeatherSet();
        world.setWeather(-1, -1, weather.getRaining(), weather.getThundering());
        Challenge.updateChallengesInCurrentWeather();
        ChallengesTicker.changeWeather();
        SeasonsAPI.getLogger().info("Weather was set to \"{}\"", weather.getId());
        if (Config.getModConfig("API").getConfig().getBoolean("conf.enable.title.title") && Boolean.FALSE.equals(weather.isNightly()))
            Title.showTitle();
    }

    public static WeatherObject getWeatherByID(String id) {
        return allWeathers.containsKey(id) ? allWeathers.get(id) : allWeathers.get("example");
    }

    public static List<WeatherObject> getAll() {
        return allWeathers.values().stream().toList();
    }

    public static void setNight() {
        isNight = true;
        setChancedWeatherInCurrentSeason();
    }
    public static void setDay() {
        isNight = false;
        setChancedWeatherInCurrentSeason();
    }

}
