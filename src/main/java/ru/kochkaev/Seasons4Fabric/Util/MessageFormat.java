package ru.kochkaev.Seasons4Fabric.Util;

import ru.kochkaev.Seasons4Fabric.Service.Season;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import net.minecraft.server.MinecraftServer;

public class MessageFormat {

    public static String formatMessage(String message){

        // Placeholders
        message = message.replaceAll("%season%", Season.getCurrent().getName());
        message = message.replaceAll("%weather%", Weather.getCurrent().getName());

        // Colors
        message = message.replaceAll("&", "§");

        return message;
    }

}
