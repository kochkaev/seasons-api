package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Arrays;

public class Beautiful extends WeatherObject {

    public Beautiful() {
        super("BEAUTIFUL",
                Config.getLang().getString("lang.weather.beautiful.name"),
                Config.getLang().getString("lang.weather.beautiful.message"),
                false, false,
                Config.getConfig().getInt("conf.weather.beautiful.chance"),
                Arrays.asList(Season.getSeasonByID("SPRING"), Season.getSeasonByID("SUMMER")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
