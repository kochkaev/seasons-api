package ru.kochkaev.Seasons4Fabric.util;

import ru.kochkaev.Seasons4Fabric.object.WeatherObject;
import ru.kochkaev.Seasons4Fabric.service.Season;
import ru.kochkaev.Seasons4Fabric.service.Weather;

public class MessageFormat {

    public static String formatMessage(String message){

        WeatherObject weather = Weather.getCurrent();

        // Placeholders
        message = message.replaceAll("%season%", Season.getCurrent().getName());
        message = message.replaceAll("%weather%", weather.getName());

        // Colors
        message = message.replaceAll("&", "§");

        return message;
    }

}
