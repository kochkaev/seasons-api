package ru.kochkaev.api.seasons;

import ru.kochkaev.api.seasons.object.ChallengeObject;
import ru.kochkaev.api.seasons.object.ConfigFileObject;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.provider.Challenge;
import ru.kochkaev.api.seasons.provider.Config;
import ru.kochkaev.api.seasons.provider.Season;
import ru.kochkaev.api.seasons.provider.Weather;
import ru.kochkaev.api.seasons.util.Parse;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class Register {

    private static final Set<Class<ConfigFileObject>> configs4Reg = new HashSet<>();
    private static final Set<Class<SeasonObject>> seasons4Reg = new HashSet<>();
    private static final Set<Class<WeatherObject>> weathers4Reg = new HashSet<>();
    private static final Set<Class<ChallengeObject>> challenges4Reg = new HashSet<>();

    public static void register() {
        try {
            for (Class<ConfigFileObject> config: configs4Reg) {
                ConfigFileObject configObject = config.getConstructor().newInstance();
                Config.getModConfig(configObject.getModName()).registerConfigObject(configObject);
            }
            if (SeasonsAPI.isStarted()){
                SeasonsAPI.getLogger().info("Registered " + configs4Reg.size() + " config files");
                for (Class<SeasonObject> season : seasons4Reg) Season.register(season.getConstructor().newInstance());
                SeasonsAPI.getLogger().info("Registered " + seasons4Reg.size() + " seasons");
                for (Class<WeatherObject> weather : weathers4Reg)
                    Weather.register(weather.getConstructor().newInstance());
                SeasonsAPI.getLogger().info("Registered " + weathers4Reg.size() + " weathers");
                for (Class<ChallengeObject> challenge : challenges4Reg)
                    Challenge.register(challenge.getConstructor().newInstance());
                SeasonsAPI.getLogger().info("Registered " + challenges4Reg.size() + " challenges");
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static void addConfig4Reg(Class<ConfigFileObject> config) { configs4Reg.add(config);}
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
    public static void registerAllAndApply(String packageName) {
        Set<Class<?>> classes = Parse.getAllClassesInPackage(packageName);
        registerAllAndApply(classes);
    }
    public static void registerAllAndApply(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            registerObjectAndApply(clazz);
        }
    }
    public static void registerObject(Class<?> clazz) {
        if (ConfigFileObject.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Class<ConfigFileObject> configObjectClass = (Class<ConfigFileObject>) clazz;
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
    public static void registerObjectAndApply(Class<?> clazz) {
        try {
            switch (clazz.getConstructor().newInstance()) {
                case ConfigFileObject t -> Config.getModConfig(t.getModName()).registerConfigObject(t);
                case SeasonObject s -> Season.register(s);
                case WeatherObject w -> Weather.register(w);
                case ChallengeObject c -> Challenge.register(c);
                default -> throw new IllegalStateException("Unexpected value: " + clazz.getConstructor().newInstance());
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException  e) {
            throw new RuntimeException(e);
        }
    }

}
