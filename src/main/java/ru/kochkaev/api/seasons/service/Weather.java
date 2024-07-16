package ru.kochkaev.api.seasons.service;

//import ru.kochkaev.seasons-api.Config.OldConfig;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import ru.kochkaev.api.seasons.ChallengesTicker;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.util.Message;

import java.util.*;

public class Weather {

    private static final Random random = new Random();

    private static WeatherObject CURRENT_WEATHER;
    private static WeatherObject CURRENT_WEATHER_PREVIOUS;
    private static final List<WeatherObject> dailyWeathers = new ArrayList<>();
    private static final List<WeatherObject> nightlyWeathers = new ArrayList<>();
    private static final List<WeatherObject> allWeathers = new ArrayList<>();
    private static boolean isNight;

    public static void register(WeatherObject weather) {
        allWeathers.add(weather);
        @Nullable
        Boolean nightly = weather.isNightly();
        if (nightly != null) {
            if (nightly) nightlyWeathers.add(weather);
            else dailyWeathers.add(weather);
        }
    }

    public static WeatherObject getChancedWeather(List<WeatherObject> weathers){
//        List<WeatherObject> weathers = isNight ? getSeasonNightlyWeathers(season) : getSeasonDailyWeathers(season);
        List<Integer> chances = new ArrayList<>();
        int maxChance = 0;
        for (WeatherObject weather : weathers){
            chances.add(weather.getChance());
            maxChance += weather.getChance();
        }
        int randInt = (int) (random.nextFloat() * (maxChance - 1) + 1);
        for (int i = 0; i<weathers.size(); i++){
            if (maxChance - randInt <= chances.get(i)) { return weathers.get(i); }
            else maxChance -= chances.get(i);
        }
        return null;
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

    public static void onServerStartup(ServerWorld world){
        String currentStr = Config.getCurrent("weather");
        String prevCurrentStr = Config.getCurrent("previous-weather");
        if (currentStr.equals("NONE")) {
            boolean isDay = world.getTimeOfDay() % 24000L < Config.getConfig().getLong("conf.tick.day.end");
            if (prevCurrentStr.equals("NONE")){
                SeasonObject season = Season.getCurrent();
                CURRENT_WEATHER = getChancedWeather(isDay ? getSeasonDailyWeathers(season) : getSeasonNightlyWeathers(season));
            }
            if (isDay) {
                setDay(world);
            }
            else setNight(world);
        }
        if (!prevCurrentStr.equals(currentStr)) CURRENT_WEATHER_PREVIOUS = getWeatherByID(prevCurrentStr);
        else {
            SeasonObject season = Season.getCurrent();
            CURRENT_WEATHER_PREVIOUS = getChancedWeather(Boolean.TRUE.equals(Objects.requireNonNull(Weather.getWeatherByID(currentStr)).isNightly()) ? getSeasonDailyWeathers(season) : getSeasonNightlyWeathers(season));
        }
        CURRENT_WEATHER = getWeatherByID(currentStr);
        assert CURRENT_WEATHER != null;
        isNight = Boolean.TRUE.equals(CURRENT_WEATHER.isNightly());
    }
    public static void saveCurrentToConfig(){
        String currentStr = CURRENT_WEATHER.getId();
        String prevCurrentStr = CURRENT_WEATHER_PREVIOUS.getId();
        Config.writeCurrent("weather", currentStr);
        Config.writeCurrent("previous-weather", prevCurrentStr);
        Config.saveCurrent();
    }

    public static void reloadFromConfig(ServerWorld world) {
        String currentStr = Config.getCurrent("weather");
        String prevCurrentStr = Config.getCurrent("previous-weather");
        WeatherObject curWeather = null;
        WeatherObject curWeatherPrev = null;
        for (WeatherObject weather : allWeathers) {
            String id = weather.getId();
            if (id.equals(prevCurrentStr)) curWeatherPrev = weather;
            else if (id.equals(currentStr)) curWeather = weather;
        }
        if (CURRENT_WEATHER != curWeather) {
            assert curWeather != null;
            setWeather(curWeather, world);
        }
        if (CURRENT_WEATHER_PREVIOUS != curWeatherPrev) {
            assert curWeatherPrev != null;
            CURRENT_WEATHER_PREVIOUS = curWeatherPrev;
        }
    }

    public static void setChancedWeatherInCurrentSeason(ServerWorld world){
        SeasonObject currentSeason = Season.getCurrent();
        WeatherObject weather = getChancedWeather(isNight ? getSeasonNightlyWeathers(currentSeason) : getSeasonDailyWeathers(currentSeason));
        assert weather != null;
        setWeather(weather, world);
    }

    public static Boolean isNight() { return isNight; }

    public static void setWeather(WeatherObject weather, ServerWorld world) {
        CURRENT_WEATHER.onWeatherRemove();
        Weather.setCurrent(weather);
        Message.sendMessage2Server(weather.getMessage(), world.getServer().getPlayerManager());
        weather.onWeatherSet();
        world.setWeather(-1, -1, weather.getRaining(), weather.getThundering());
        Challenge.updateChallengesInCurrentWeather();
        ChallengesTicker.changeWeather();
        //for (ChallengeObject effect : Challenge.getChallengesInCurrentWeather()) Message.sendMessage2Server(effect.getTriggerMessage(), world.getServer().getPlayerManager());
    }

    public static WeatherObject getWeatherByID(String id) {
        for (WeatherObject weather : allWeathers) if (weather.getId().equals(id)) return weather;
        return null;
    }

    public static List<WeatherObject> getAll() {
        return allWeathers;
    }

    public static void setNight(ServerWorld world) {
        isNight = true;
        setChancedWeatherInCurrentSeason(world);
    }
    public static void setDay(ServerWorld world) {
        isNight = false;
        setChancedWeatherInCurrentSeason(world);
    }

}
