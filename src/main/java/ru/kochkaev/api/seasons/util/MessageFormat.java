package ru.kochkaev.api.seasons.util;

import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;

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
