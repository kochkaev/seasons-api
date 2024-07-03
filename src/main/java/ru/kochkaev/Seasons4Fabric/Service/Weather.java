package ru.kochkaev.Seasons4Fabric.Service;

//import ru.kochkaev.Seasons4Fabric.Config.OldConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Util.Message;
import ru.kochkaev.Seasons4Fabric.Util.WeatherUtils;

import java.util.*;

public enum Weather {
    
    NIGHT(Config.getString("lang.weather.night.name"),
            Config.getString("lang.weather.night.message"),
            false, false, false, 0, Collections.emptyList()),
    SNOWY(Config.getString("lang.weather.snowy.name"),
            Config.getString("lang.weather.snowy.message"),
            false, true, false,
            Config.getInt("conf.weather.snowy.chance"),
            Collections.singletonList(Season.WINTER)),
    FREEZING(Config.getString("lang.weather.freezing.name"),
            Config.getString("lang.weather.freezing.message"),
            true, false, false,
            Config.getInt("conf.weather.freezing.chance"),
            Collections.singletonList(Season.WINTER)),
    STORMY(Config.getString("lang.weather.stormy.name"),
            Config.getString("lang.weather.stormy.message"),
            true, true, true,
            Config.getInt("conf.weather.stormy.chance"),
            Collections.singletonList(Season.FALL)),
    COLD(Config.getString("lang.weather.cold.name"),
            Config.getString("lang.weather.cold.message"),
            false, false, false,
            Config.getInt("conf.weather.cold.chance"),
            Arrays.asList(Season.WINTER, Season.FALL)),
    WARM(Config.getString("lang.weather.warm.name"),
            Config.getString("lang.weather.warm.message"),
            false, false, false,
            Config.getInt("conf.weather.warm.chance"),
            Collections.singletonList(Season.SUMMER)),
    HOT(Config.getString("lang.weather.hot.name"),
            Config.getString("lang.weather.hot.message"),
            false, false, false,
            Config.getInt("conf.weather.hot.chance"),
            Collections.singletonList(Season.SUMMER)),
    SCORCHING(Config.getString("lang.weather.scorching.name"),
            Config.getString("lang.weather.scorching.message"),
            false, false, false,
            Config.getInt("conf.weather.scorching.chance"),
            Collections.singletonList(Season.SUMMER)),
    RAINY(Config.getString("lang.weather.rainy.name"),
            Config.getString("lang.weather.rainy.message"),
            false, true, false,
            Config.getInt("conf.weather.rainy.chance"),
            Arrays.asList(Season.WINTER, Season.SPRING, Season.FALL)),
    CHILLY(Config.getString("lang.weather.chilly.name"),
            Config.getString("lang.weather.chilly.message"),
            false, false, false,
            Config.getInt("conf.weather.chilly.chance"),
            Collections.singletonList(Season.SPRING)),
    BREEZY(Config.getString("lang.weather.breezy.name"),
            Config.getString("lang.weather.breezy.message"),
            false, false, false,
            Config.getInt("conf.weather.breezy.chance"),
            Arrays.asList(Season.SPRING, Season.FALL)),
    BEAUTIFUL(Config.getString("lang.weather.beautiful.name"),
            Config.getString("lang.weather.beautiful.message"),
            false, false, false,
            Config.getInt("conf.weather.beautiful.chance"),
            Arrays.asList(Season.SPRING, Season.SUMMER));

    private static final Random random = new Random();

    private final String name; // Default name shown to players
    private final String message; // Default message to show the weather changing
    private final boolean catastrophic; // Is there is a high risk of this weather killing a player?
    private final boolean raining; // Whether there should be rain
    private final boolean thundering; // Whether the rain should be thunderous
    private final int chance;
    private final List<Season> seasons; // List of seasons this weather can be triggered on

    private static Weather CURRENT_WEATHER;

    Weather(String name, String broadcast, boolean catastrophic, boolean raining, boolean thundering, int chance, List<Season> seasons) {
        this.name = name;
        this.message = broadcast;
        this.catastrophic = catastrophic;
        this.raining = raining;
        this.thundering = thundering;
        this.chance = chance;
        this.seasons = seasons;
    }

    public static Weather getChancedWeather(Season season){
        List<Weather> weathers = getSeasonWeathers(season);
        List<Integer> chances = new ArrayList<>();
        int maxChance = 0;
        for (Weather weather : weathers){
            chances.add(weather.chance);
            maxChance += weather.chance;
        }
        int randInt = (int) (random.nextFloat() * (maxChance - 1) + 1);
        for (int i = 0; i<weathers.size(); i++){
            if (maxChance - randInt <= chances.get(i)) { return weathers.get(i); }
            else maxChance -= chances.get(i);
        }
        return null;
    }

    public static List<Weather> getSeasonWeathers(Season season){
        List<Weather> weathers = new ArrayList<>();
        for (Weather weather : values()){
            if (weather.getSeasons().contains(season)) { weathers.add(weather); }
        }
        return weathers;
    }

    public static Weather getCurrent(){
        return CURRENT_WEATHER;
    }

    public static void setCurrent(Weather weather){
        CURRENT_WEATHER = weather;
    }

    public static void restoreCurrentFromConfig(){
        String currentStr = Config.getCurrent("weather");
        CURRENT_WEATHER = valueOf(currentStr);
    }
    public static void saveCurrentToConfig(){
        String currentStr = CURRENT_WEATHER.toString();
        Config.writeCurrent("weather", currentStr);
    }

    public static void setChancedWeatherInCurrentSeason(ServerWorld world){
        Season currentSeason = Season.getCurrent();
        Weather weather = Weather.getChancedWeather(currentSeason);
        setWeather(weather, world);
    }

    public String getName() { return this.name; }
    public String  getMessage() { return this.message; }
    public boolean getRaining() { return this.raining; }
    public boolean getThundering() { return this.thundering; }
    public List<Season> getSeasons() { return this.seasons; }

    public static Boolean isNight() { return CURRENT_WEATHER == NIGHT; }
    public static void setWeather(Weather weather, ServerWorld world) {
        Weather.setCurrent(weather);
        Message.sendNewMessage(weather.getMessage(), world.getServer().getPlayerManager());
        WeatherUtils.setWeather(weather, world);
    }

    public static Weather getWeatherViaID(String id) { return valueOf(id); }
}
