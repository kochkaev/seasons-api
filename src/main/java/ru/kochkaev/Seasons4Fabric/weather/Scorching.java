package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Collections;

public class Scorching extends WeatherObject {

    public Scorching() {
        super("SCORCHING",
                Config.getLang().getString("lang.weather.scorching.name"),
                Config.getLang().getString("lang.weather.scorching.message"),
                false, false,
                Config.getConfig().getInt("conf.weather.scorching.chance"),
                Collections.singletonList(Season.getSeasonByID("SUMMER")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
