package ru.kochkaev.Seasons4Fabric.Util;

import ru.kochkaev.Seasons4Fabric.Service.Season;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import net.minecraft.server.MinecraftServer;

public class MessageFormat {

    public static String formatMessage(String message){

        Weather weather = Weather.isNight() ? Weather.NIGHT : Weather.getCurrent();

        // Placeholders
        message = message.replaceAll("%season%", Season.getCurrent().getName());
        message = message.replaceAll("%weather%", weather.getName());

        // Colors
        message = message.replaceAll("&", "§");

        return message;
    }

}
