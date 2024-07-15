package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Arrays;
import java.util.Collections;

public class Rainy extends WeatherObject {

    public Rainy() {
        super("RAINY",
                Config.getLang().getString("lang.weather.rainy.name"),
                Config.getLang().getString("lang.weather.rainy.message"),
                true, false,
                Config.getConfig().getInt("conf.weather.rainy.chance"),
                Arrays.asList(Season.getSeasonByID("SPRING"), Season.getSeasonByID("FALL")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
