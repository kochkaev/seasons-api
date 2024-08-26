package ru.kochkaev.api.seasons;

import ru.kochkaev.api.seasons.service.Config;
import ru.kochkaev.api.seasons.object.ChallengeObject;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.object.TXTConfigObject;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Challenge;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.Parse;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class Register {

    private static final Set<Class<TXTConfigObject>> configs4Reg = new HashSet<>();
    private static final Set<Class<SeasonObject>> seasons4Reg = new HashSet<>();
    private static final Set<Class<WeatherObject>> weathers4Reg = new HashSet<>();
    private static final Set<Class<ChallengeObject>> challenges4Reg = new HashSet<>();

    public static void register() {
        try {
            for (Class<TXTConfigObject> config: configs4Reg) {
                TXTConfigObject configObject = config.getConstructor().newInstance();
                Config.getModConfig(configObject.getModName()).registerConfigObject(configObject);
            }
            Config.initConfigObjects();
            SeasonsAPI.getLogger().info("Registered {} config files", configs4Reg.size());
            for (Class<SeasonObject> season: seasons4Reg) Season.register(season.getConstructor().newInstance());
            SeasonsAPI.getLogger().info("Registered {} seasons", seasons4Reg.size());
            for (Class<WeatherObject> weather : weathers4Reg) Weather.register(weather.getConstructor().newInstance());
            SeasonsAPI.getLogger().info("Registered {} weathers", weathers4Reg.size());
            for (Class<ChallengeObject> challenge : challenges4Reg) Challenge.register(challenge.getConstructor().newInstance());
            SeasonsAPI.getLogger().info("Registered {} challenges", challenges4Reg.size());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static void addConfig4Reg(Class<TXTConfigObject> config) { configs4Reg.add(config);}
    public static void addSeason4Reg(Class<SeasonObject> season) { seasons4Reg.add(season);}
    public static void addWeather4Reg(Class<WeatherObject> weather) { weathers4Reg.add(weather);}
    public static void addChallenge4Reg(Class<ChallengeObject> challenge) { challenges4Reg.add(challenge);}

    public static void registerAllInPackage(String packageName) {
        Set<Class<?>> classes = Parse.getAllClassesInPackage(packageName);
        registerAllInSet(classes);
    }
    public static void registerAllInSet(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            registerObject(clazz);
        }
    }
    public static void registerObject(Class<?> clazz) {
        if (TXTConfigObject.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Class<TXTConfigObject> configObjectClass = (Class<TXTConfigObject>) clazz;
            addConfig4Reg(configObjectClass);
        } else if (SeasonObject.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Class<SeasonObject> seasonObjectClass = (Class<SeasonObject>) clazz;
            addSeason4Reg(seasonObjectClass);
        } else if (WeatherObject.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Class<WeatherObject> weatherObjectClass = (Class<WeatherObject>) clazz;
            addWeather4Reg(weatherObjectClass);
        } else if (ChallengeObject.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Class<ChallengeObject> challengeObjectClass = (Class<ChallengeObject>) clazz;
            addChallenge4Reg(challengeObjectClass);
        }
    }

}
