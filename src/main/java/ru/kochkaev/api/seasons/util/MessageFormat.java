package ru.kochkaev.api.seasons.util;

import org.apache.commons.lang3.StringUtils;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;

import java.util.Map;

public class MessageFormat {

    public static String formatMessage(String message, Map<String, String> map){

//        WeatherObject weather = Weather.getCurrent();

        // Placeholders
        for (String placeholder : map.keySet()) {
            message = message.replaceAll(placeholder, map.get(placeholder));
        }
//        for (int i = 0; i < StringUtils.countMatches(message, "%"); i++) {
//            String placeholder = message.substring(message.indexOf("%"), message.indexOf("%",1)+1);
//            if (map.containsKey(placeholder)) {
//                message = message.replace(placeholder, map.get(placeholder));
//            }
//        }
//        message = message.replaceAll("%season%", Season.getCurrent().getName());
//        message = message.replaceAll("%weather%", weather.getName());

        // Colors
        message = message.replaceAll("&", "§");

        return message;
    }

}
