package ru.kochkaev.api.seasons;

import ru.kochkaev.api.seasons.object.ChallengeObject;
import ru.kochkaev.api.seasons.object.EventObject;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Challenge;
import ru.kochkaev.api.seasons.service.Event;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.ParseClassesInPackage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Register {

    private static final List<SeasonObject> seasons = new ArrayList<>();
    private static final List<WeatherObject> weathers = new ArrayList<>();
    private static final List<ChallengeObject> challenges = new ArrayList<>();
    private static final List<EventObject> events = new ArrayList<>();
    private static final Set<Class<SeasonObject>> seasons4Reg = new HashSet<>();
    private static final Set<Class<WeatherObject>> weathers4Reg = new HashSet<>();
    private static final Set<Class<ChallengeObject>> challenges4Reg = new HashSet<>();
    private static final Set<Class<EventObject>> events4Reg = new HashSet<>();

    public static void register() {
        try {
            for (Class<EventObject> event : events4Reg) Event.register(event.getConstructor().newInstance());
            for (Class<SeasonObject> season: seasons4Reg) Season.register(season.getConstructor().newInstance());
            for (Class<WeatherObject> weather : weathers4Reg) Weather.register(weather.getConstructor().newInstance());
            for (Class<ChallengeObject> challenge : challenges4Reg) Challenge.register(challenge.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
//        for (EventObject event : events) Event.register(event);
//        for (SeasonObject season : seasons) Season.register(season);
//        for (WeatherObject weather : weathers) Weather.register(weather);
//        for (ChallengeObject challenge : challenges) Challenge.register(challenge);
    }

    public static void addEvent(EventObject event) { events.add(event);}
    public static void addSeason(SeasonObject season) { seasons.add(season);}
    public static void addWeather(WeatherObject weather) { weathers.add(weather);}
    public static void addChallenge(ChallengeObject challenge) { challenges.add(challenge);}
    public static void addEvent4Reg(Class<EventObject> event) { events4Reg.add(event);}
    public static void addSeason4Reg(Class<SeasonObject> season) { seasons4Reg.add(season);}
    public static void addWeather4Reg(Class<WeatherObject> weather) { weathers4Reg.add(weather);}
    public static void addChallenge4Reg(Class<ChallengeObject> challenge) { challenges4Reg.add(challenge);}

    public static void registerAllInPackage(String packageName) {
        Set<Class<?>> classes = ParseClassesInPackage.getAllClassesInPackage(packageName);
        registerAllInSet(classes);
    }
    public static void registerAllInSet(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            registerObject(clazz);
        }
    }
    public static void registerObject(Class<?> clazz) {
        if (EventObject.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Class<EventObject> eventObjectClass = (Class<EventObject>) clazz;
            addEvent4Reg(eventObjectClass);
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
