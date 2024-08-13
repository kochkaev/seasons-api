package ru.kochkaev.api.seasons.util;

import java.util.Map;

public class Format {

    public static String formatMessage(String message, Map<String, String> placeholders){

        // Placeholders
        for (String placeholder : placeholders.keySet()) {
            message = message.replaceAll(placeholder, placeholders.get(placeholder));
        }

        // Colors
        message = message.replaceAll("&", "§");

        return message;
    }

}
