package ru.kochkaev.api.seasons.util;

import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.util.functional.IFuncStringRet;
import ru.kochkaev.api.seasons.util.functional.IFuncTextRet;

import java.util.HashMap;
import java.util.Map;

public class Format {

    private static final Map<String, IFuncStringRet> placeholders = new HashMap<>();

    public static String formatMessage(String message, Map<String, String> placeholders){

        if (SeasonsAPI.getPlaceholderAPI() != null) {
            message = SeasonsAPI.getPlaceholderAPI().parseString(message);
        }
        else {
            // Placeholders
            for (String placeholder : placeholders.keySet()) {
                message = message.replaceAll(placeholder, placeholders.get(placeholder));
            }
        }

        // Colors
        message = message.replaceAll("&", "§");

        return message;
    }

    public static String formatMessage(String message){

        if (SeasonsAPI.getPlaceholderAPI() != null) {
            message = SeasonsAPI.getPlaceholderAPI().parseString(message);
        }
        else {
            // Placeholders
            for (String key : placeholders.keySet()) if (message.contains(key)) message = message.replaceAll(key, placeholders.get(key).function());
        }

        // Colors
        message = message.replaceAll("&", "§");

        return message;
    }
    public static Text formatTextMessage(Text message){

        if (SeasonsAPI.getPlaceholderAPI() != null) {
            message = SeasonsAPI.getPlaceholderAPI().parseText(message);
        }
        else {
            String tempMsg = message.getString();
            // Placeholders
            for (String placeholder : placeholders.keySet()) {
                tempMsg = tempMsg.replaceAll(placeholder, placeholders.get(placeholder).function());
            }
            message = Text.of(tempMsg);
        }

        // Colors
        message = Text.of(message.getString().replaceAll("&", "§"));

        return message;
    }
    private static String doubleParseFormat(String value){
        for (String key : placeholders.keySet()) if (value.contains(key)) value = value.replaceAll(key, placeholders.get(key).function());
        return value;
    }

    public static void regDynamicPlaceholder(String placeholder, IFuncStringRet lmbd){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().register(placeholder, lmbd);
        else placeholders.put("%seasons:"+placeholder+"%", lmbd);
    }
    public static void regStaticPlaceholder(String placeholder, String value){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().register(placeholder, value);
        else placeholders.put("%seasons:"+placeholder+"%", () -> value);
    }
    public static void regDoubleParseDynamicPlaceholder(String placeholder, IFuncTextRet lmbd){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, lmbd);
        else placeholders.put("%seasons:"+placeholder+"%", () -> doubleParseFormat(lmbd.function().getString()));
    }
    public static void regDoubleParseDynamicPlaceholder(String placeholder, IFuncStringRet lmbd){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, () -> Text.of(lmbd.function()));
        else placeholders.put("%seasons:"+placeholder+"%", () -> doubleParseFormat(lmbd.function()));
    }
    public static void regDoubleParseStaticPlaceholder(String placeholder, String value){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, Text.of(value));
        else placeholders.put("%seasons:"+placeholder+"%", () -> doubleParseFormat(value));
    }

}
