package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Arrays;
import java.util.Collections;

public class Breezy extends WeatherObject {

    public Breezy() {
        super("BREEZY",
                Config.getLang().getString("lang.weather.breezy.name"),
                Config.getLang().getString("lang.weather.breezy.message"),
                false, false,
                Config.getConfig().getInt("conf.weather.breezy.chance"),
                Arrays.asList(Season.getSeasonByID("SPRING"), Season.getSeasonByID("FALL")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
