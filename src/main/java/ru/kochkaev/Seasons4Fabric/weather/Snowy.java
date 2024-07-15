package ru.kochkaev.Seasons4Fabric.weather;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;

import java.util.Collections;

public class Snowy extends WeatherObject {

    public Snowy() {
        super("SNOWY",
                Config.getLang().getString("lang.weather.snowy.name"),
                Config.getLang().getString("lang.weather.snowy.message"),
                true, false,
                Config.getConfig().getInt("conf.weather.snowy.chance"),
                Collections.singletonList(Season.getSeasonByID("WINTER")), false);
    }

    @Override
    public void onWeatherSet() {

    }

    @Override
    public void onWeatherRemove() {

    }
}
