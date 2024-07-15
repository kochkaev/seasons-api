package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Collections;

public class Cold extends WeatherObject {

    public Cold() {
        super("COLD",
                Config.getLang().getString("lang.weather.cold.name"),
                Config.getLang().getString("lang.weather.cold.message"),
                false, false,
                Config.getConfig().getInt("conf.weather.cold.chance"),
                Collections.singletonList(Season.getSeasonByID("FALL")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
