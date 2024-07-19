package ru.kochkaev.api.seasons.example;

import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Season;

import java.util.Collections;

public class ExampleWeather extends WeatherObject {
    public ExampleWeather() {
        super("example", "No weathers matched", "No available weathers matched. Try to download additional mod based on Seasons API.", false, false, 0, Collections.singletonList(Season.getSeasonByID("example")), null);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
