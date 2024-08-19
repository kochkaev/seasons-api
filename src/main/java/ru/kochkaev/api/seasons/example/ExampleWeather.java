package ru.kochkaev.api.seasons.example;

import net.minecraft.server.MinecraftServer;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Season;

import java.util.Collections;

public class ExampleWeather extends WeatherObject {
    public ExampleWeather() {
        super("example", () -> "No weathers matched", false, false, 0, Collections.singletonList(Season.getSeasonByID("example")), null);
    }

    @Override
    public void onWeatherSet() {
        sendMessage("No available weathers matched. Try to download additional mod based on Seasons API.");
    }

    @Override
    public void onWeatherRemove() {

    }
}
