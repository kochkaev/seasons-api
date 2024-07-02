package ru.kochkaev.Seasons4Fabric.Service;

import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Execute.Message;
import ru.kochkaev.Seasons4Fabric.Execute.WeatherExecutor;
import ru.kochkaev.Seasons4Fabric.Temp;

import java.util.*;

public enum Weather {
    
    NIGHT(Config.LANG_WEATHER_NIGHT_NAME,
            Config.LANG_WEATHER_NIGHT_MESSAGE,
            false, false, false, 0,
            Arrays.asList(Season.WINTER, Season.SPRING, Season.SUMMER, Season.FALL)),
    SNOWY(Config.LANG_WEATHER_SNOWY_NAME,
            Config.LANG_WEATHER_SNOWY_MESSAGE,
            false, true, false, Config.CONF_WEATHER_SNOWY_CHANCE,
            Collections.singletonList(Season.WINTER)),
    FREEZING(Config.LANG_WEATHER_FREEZING_NAME,
            Config.LANG_WEATHER_FREEZING_MESSAGE,
            true, false, false, Config.CONF_WEATHER_FREEZING_CHANCE,
            Collections.singletonList(Season.WINTER)),
    STORMY(Config.LANG_WEATHER_STORMY_NAME,
            Config.LANG_WEATHER_STORMY_MESSAGE,
            true, true, true, Config.CONF_WEATHER_STORMY_CHANCE,
            Collections.singletonList(Season.FALL)),
    COLD(Config.LANG_WEATHER_COLD_NAME,
            Config.LANG_WEATHER_COLD_MESSAGE,
            false, false, false, Config.CONF_WEATHER_COLD_CHANCE,
            Arrays.asList(Season.WINTER, Season.FALL)),
    WARM(Config.LANG_WEATHER_WARM_NAME,
            Config.LANG_WEATHER_WARM_MESSAGE,
            false, false, false, Config.CONF_WEATHER_WARM_CHANCE,
            Collections.singletonList(Season.SUMMER)),
    HOT(Config.LANG_WEATHER_HOT_NAME,
            Config.LANG_WEATHER_HOT_MESSAGE,
            false, false, false, Config.CONF_WEATHER_HOT_CHANCE,
            Collections.singletonList(Season.SUMMER)),
    SCORCHING(Config.LANG_WEATHER_SCORCHING_NAME,
            Config.LANG_WEATHER_SCORCHING_MESSAGE,
            false, false, false, Config.CONF_WEATHER_SCORCHING_CHANCE,
            Collections.singletonList(Season.SUMMER)),
    RAINY(Config.LANG_WEATHER_RAINY_NAME,
            Config.LANG_WEATHER_RAINY_MESSAGE,
            false, true, false, Config.CONF_WEATHER_RAINY_CHANCE,
            Arrays.asList(Season.WINTER, Season.SPRING, Season.FALL)),
    CHILLY(Config.LANG_WEATHER_CHILLY_NAME,
            Config.LANG_WEATHER_CHILLY_MESSAGE,
            false, false, false, Config.CONF_WEATHER_CHILLY_CHANCE,
            Collections.singletonList(Season.SPRING)),
    BREEZY(Config.LANG_WEATHER_BREEZY_NAME,
            Config.LANG_WEATHER_BREEZY_MESSAGE,
            false, false, false, Config.CONF_WEATHER_BREEZY_CHANCE,
            Arrays.asList(Season.SPRING, Season.FALL)),
    BEAUTIFUL(Config.LANG_WEATHER_BEAUTIFUL_NAME,
            Config.LANG_WEATHER_BEAUTIFUL_MESSAGE,
            false, false, false, Config.CONF_WEATHER_BEAUTIFUL_CHANCE,
            Arrays.asList(Season.SPRING, Season.SUMMER));

    private static final Random random = new Random();

    private final String name; // Default name shown to players
    private final String message; // Default message to show the weather changing
    private final boolean catastrophic; // Is there is a high risk of this weather killing a player?
    private final boolean storm; // Whether there should be rain
    private final boolean thundering; // Whether the rain should be thunderous
    private final int chance;
    private final List<Season> seasons; // List of seasons this weather can be triggered on

    private static Weather CURRENT_WEATHER;

    Weather(String name, String broadcast, boolean catastrophic, boolean storm, boolean thundering, int chance, List<Season> seasons) {
        this.name = name;
        this.message = broadcast;
        this.catastrophic = catastrophic;
        this.storm = storm;
        this.thundering = thundering;
        this.chance = chance;
        this.seasons = seasons;
    }

    public static Weather getChancedWeather(Season season){
        List<Weather> weathers = getSeasonWeathers(season);
        List<Integer> chances = null;
        int maxChance = 0;
        for (Weather weather : weathers){
            chances.add(weather.chance);
            maxChance += weather.chance;
        }
        int randInt = (int) (random.nextFloat() * (maxChance - 1) + 1);
        for (int i = 0; i<weathers.size(); i++){
            if (randInt<= chances.get(i)) { return weathers.get(i); }
        }
        return null;
    }

    public static List<Weather> getSeasonWeathers(Season season){
        List<Weather> weathers = null;
        for (Weather weather : values()){
            if (weather.seasons.contains(season)) { weathers.add(weather); }
        }
        return weathers;
    }

    public static Weather getCurrent(){
        return CURRENT_WEATHER;
    }

    public static void setCurrent(Weather weather){
        CURRENT_WEATHER = weather;
    }

    public static void setChancedWeatherInCurrentSeason(){
        Season currentSeason = Season.getCurrent();
        Weather weather = Weather.getChancedWeather(currentSeason);
        Weather.setCurrent(weather);
        Message.getInstance().sendMessage(weather.message);
        WeatherExecutor.setWeather(weather);
    }
}
