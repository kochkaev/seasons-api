package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Collections;

public class Hot extends WeatherObject {

    public Hot() {
        super("HOT",
                Config.getLang().getString("lang.weather.hot.name"),
                Config.getLang().getString("lang.weather.hot.message"),
                false, false,
                Config.getConfig().getInt("conf.weather.hot.chance"),
                Collections.singletonList(Season.getSeasonByID("SUMMER")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
