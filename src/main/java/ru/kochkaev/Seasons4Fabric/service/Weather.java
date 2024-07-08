package ru.kochkaev.Seasons4Fabric.service;

//import ru.kochkaev.Seasons4Fabric.Config.OldConfig;
import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.util.Message;
import ru.kochkaev.Seasons4Fabric.util.WeatherUtils;

import java.util.*;

public enum Weather {
    
    NIGHT("lang.weather.night.name",
            "lang.weather.night.message",
            false, false, false, "", Collections.emptyList()),
    SNOWY("lang.weather.snowy.name",
            "lang.weather.snowy.message",
            false, true, false,
            "conf.weather.snowy.chance",
            Collections.singletonList(Season.WINTER)),
    FREEZING("lang.weather.freezing.name",
            "lang.weather.freezing.message",
            true, false, false,
            "conf.weather.freezing.chance",
            Collections.singletonList(Season.WINTER)),
    STORMY("lang.weather.stormy.name",
            "lang.weather.stormy.message",
            true, true, true,
            "conf.weather.stormy.chance",
            Collections.singletonList(Season.FALL)),
    COLD("lang.weather.cold.name",
            "lang.weather.cold.message",
            false, false, false,
            "conf.weather.cold.chance",
            Arrays.asList(Season.WINTER, Season.FALL)),
    WARM("lang.weather.warm.name",
            "lang.weather.warm.message",
            false, false, false,
            "conf.weather.warm.chance",
            Collections.singletonList(Season.SUMMER)),
    HOT("lang.weather.hot.name",
            "lang.weather.hot.message",
            false, false, false,
            "conf.weather.hot.chance",
            Collections.singletonList(Season.SUMMER)),
    SCORCHING("lang.weather.scorching.name",
            "lang.weather.scorching.message",
            false, false, false,
            "conf.weather.scorching.chance",
            Collections.singletonList(Season.SUMMER)),
    RAINY("lang.weather.rainy.name",
            "lang.weather.rainy.message",
            false, true, false,
            "conf.weather.rainy.chance",
            Arrays.asList(Season.WINTER, Season.SPRING, Season.FALL)),
    CHILLY("lang.weather.chilly.name",
            "lang.weather.chilly.message",
            false, false, false,
            "conf.weather.chilly.chance",
            Collections.singletonList(Season.SPRING)),
    BREEZY("lang.weather.breezy.name",
            "lang.weather.breezy.message",
            false, false, false,
            "conf.weather.breezy.chance",
            Arrays.asList(Season.SPRING, Season.FALL)),
    BEAUTIFUL("lang.weather.beautiful.name",
            "lang.weather.beautiful.message",
            false, false, false,
            "conf.weather.beautiful.chance",
            Arrays.asList(Season.SPRING, Season.SUMMER));

    private static final Random random = new Random();

    private final String nameKey; // Default name shown to players
    private final String messageKey; // Default message to show the weather changing
    private final boolean catastrophic; // Is there is a high risk of this weather killing a player?
    private final boolean raining; // Whether there should be rain
    private final boolean thundering; // Whether the rain should be thunderous
    private final String chanceKey;
    private final List<Season> seasons; // List of seasons this weather can be triggered on

    private static Weather CURRENT_WEATHER;
    private static boolean isNight;

    Weather(String nameKey, String messageKey, boolean catastrophic, boolean raining, boolean thundering, String chanceKey, List<Season> seasons) {
        this.nameKey = nameKey;
        this.messageKey = messageKey;
        this.catastrophic = catastrophic;
        this.raining = raining;
        this.thundering = thundering;
        this.chanceKey = chanceKey;
        this.seasons = seasons;
    }

    public static Weather getChancedWeather(Season season){
        List<Weather> weathers = getSeasonWeathers(season);
        List<Integer> chances = new ArrayList<>();
        int maxChance = 0;
        for (Weather weather : weathers){
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
        saveCurrentToConfig();
    }

    public static void restoreCurrentFromConfig(){
        String currentStr = Config.getCurrent("weather");
        CURRENT_WEATHER = valueOf(currentStr);
    }
    public static void saveCurrentToConfig(){
        String currentStr = CURRENT_WEATHER.toString();
        Config.writeCurrent("weather", currentStr);
        Config.saveCurrent();
    }

    public static void reloadFromConfig(ServerWorld world) {
        String currentStr = Config.getCurrent("weather");
        if (CURRENT_WEATHER != valueOf(currentStr)) {
            setWeather(valueOf(currentStr), world);
        }
    }

    public static void setChancedWeatherInCurrentSeason(ServerWorld world){
        Season currentSeason = Season.getCurrent();
        Weather weather = Weather.getChancedWeather(currentSeason);
        setWeather(weather, world);
    }

    public String getName() { return Config.getLang().getString(this.nameKey); }
    public String  getMessage() { return Config.getLang().getString(this.messageKey); }
    public int  getChance() { return Config.getConfig().getInt(this.chanceKey); }
    public boolean getRaining() { return this.raining; }
    public boolean getThundering() { return this.thundering; }
    public List<Season> getSeasons() { return this.seasons; }

    public static Boolean isNight() { return isNight; }
    public static void setWeather(Weather weather, ServerWorld world) {
        Weather.setCurrent(weather);
        Message.sendMessage2Server(weather.getMessage(), world.getServer().getPlayerManager());
        WeatherUtils.setWeather(weather, world);
        Challenge.updateChallengesInCurrentWeather();
        for (ChallengeObject effect : Challenge.getChallengesInCurrentWeather()) Message.sendMessage2Server(effect.getTriggerMessage(), world.getServer().getPlayerManager());
    }

    public static Weather getWeatherByID(String id) { return valueOf(id); }

    public static Weather[] getAll() {
        return values();
    }

    public static void setNight(ServerWorld world) {
        isNight = true;
        Message.sendMessage2Server(Weather.NIGHT.getMessage(), world.getServer().getPlayerManager());
    }
    public static void setDay(ServerWorld world) {
        isNight = false;
        setChancedWeatherInCurrentSeason(world);
    }

}
