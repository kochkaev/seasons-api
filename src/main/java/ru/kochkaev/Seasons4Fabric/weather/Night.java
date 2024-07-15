package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Night extends WeatherObject {

    public Night() {
        super("NIGHT",
                Config.getLang().getString("lang.weather.night.name"),
                Config.getLang().getString("lang.weather.night.message"),
                null, null,
                Config.getConfig().getInt("conf.weather.night.chance"),
                Arrays.asList(Season.getSeasonByID("WINTER"), Season.getSeasonByID("SPRING"), Season.getSeasonByID("SUMMER"), Season.getSeasonByID("FALL")), true);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
