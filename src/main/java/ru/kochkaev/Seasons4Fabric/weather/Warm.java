package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Collections;

public class Warm extends WeatherObject {

    public Warm() {
        super("WARM",
                Config.getLang().getString("lang.weather.warm.name"),
                Config.getLang().getString("lang.weather.warm.message"),
                false, false,
                Config.getConfig().getInt("conf.weather.warm.chance"),
                Collections.singletonList(Season.getSeasonByID("SUMMER")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
