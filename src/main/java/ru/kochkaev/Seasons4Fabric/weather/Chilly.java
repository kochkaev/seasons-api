package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Collections;

public class Chilly extends WeatherObject {

    public Chilly() {
        super("CHILLY",
                Config.getLang().getString("lang.weather.chilly.name"),
                Config.getLang().getString("lang.weather.chilly.message"),
                false, false,
                Config.getConfig().getInt("conf.weather.chilly.chance"),
                Collections.singletonList(Season.getSeasonByID("SPRING")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
