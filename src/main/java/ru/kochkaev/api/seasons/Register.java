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

import java.util.ArrayList;
import java.util.List;

public class Register {

    private static final List<SeasonObject> seasons = new ArrayList<>();
    private static final List<WeatherObject> weathers = new ArrayList<>();
    private static final List<ChallengeObject> challenges = new ArrayList<>();
    private static final List<EventObject> events = new ArrayList<>();

    public static void register() {
        for (EventObject event : events) Event.register(event);
        for (SeasonObject season : seasons) Season.register(season);
        for (WeatherObject weather : weathers) Weather.register(weather);
        for (ChallengeObject challenge : challenges) Challenge.register(challenge);
    }

    public static void addEvent(EventObject event) { events.add(event);}
    public static void addSeason(SeasonObject season) { seasons.add(season);}
    public static void addWeather(WeatherObject weather) { weathers.add(weather);}
    public static void addChallenge(ChallengeObject challenge) { challenges.add(challenge);}

    public static void registerAllInPackage(String packageName) {
        List<Object> objects = ParseClassesInPackage.getAllClassesInPackage(packageName);
        registerAllInList(objects);
    }
    public static void registerAllInList(List<Object> objects) {
        for (Object object : objects) {
            registerObject(object);
        }
    }
    public static void registerObject(Object object) {
        if (object instanceof EventObject) {
            addEvent((EventObject) object);
        } else if (object instanceof SeasonObject) {
            addSeason((SeasonObject) object);
        } else if (object instanceof WeatherObject) {
            addWeather((WeatherObject) object);
        } else if (object instanceof ChallengeObject) {
            addChallenge((ChallengeObject) object);
        }
    }

}
