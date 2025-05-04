package ru.kochkaev.api.seasons.util;

import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.SeasonsAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Format {

    @Deprecated
    public static String formatMessage(String message, Map<String, String> placeholders){
        message = SeasonsAPI.getPlaceholderAPI().parseString(message);
        for (var key: placeholders.keySet()) {
            message = message.replaceAll(key, placeholders.get(key));
        }
        message = message.replaceAll("&", "§");
        return message;
    }

    @Deprecated
    public static String formatMessage(String message){
        return formatMessage(message, new HashMap<>());
    }
    public static Text formatTextMessage(Text message){
        message = SeasonsAPI.getPlaceholderAPI().parseText(message);
        return message;
    }
    public static Text formatTextMessage(Text message, Map<String, Text> placeholders){
        message = SeasonsAPI.getPlaceholderAPI().parseText(message, placeholders);
        return message;
    }
    private static String doubleParseFormat(String value){
        value = SeasonsAPI.getPlaceholderAPI().parseString(value);
        return value;
    }

    public static void regDynamicTextPlaceholder(String placeholder, Supplier<Text> supplier){
        SeasonsAPI.getPlaceholderAPI().register(placeholder, SeasonsAPI.getPlaceholderAPI().getTextSupplier(supplier));
    }
    public static void regStaticPlaceholder(String placeholder, Text value){
        SeasonsAPI.getPlaceholderAPI().register(placeholder, value);
    }

    public static void regDynamicPlaceholder(String placeholder, Supplier<String> supplier){
        SeasonsAPI.getPlaceholderAPI().register(placeholder, SeasonsAPI.getPlaceholderAPI().getStringSupplier(supplier));
    }
    public static void regStaticPlaceholder(String placeholder, String value){
        SeasonsAPI.getPlaceholderAPI().register(placeholder, value);
    }
    @SuppressWarnings("unchecked")
    public static void regDoubleParseDynamicPlaceholder(String placeholder, Supplier<?> supplier){
        switch (supplier.get()) {
            case String s -> {
                SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, () -> Text.of((String) supplier.get()));
            }
            case Text t -> {
                SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, SeasonsAPI.getPlaceholderAPI().getTextSupplier((Supplier<Text>) supplier));
            }
            default -> throw new IllegalStateException("Unexpected value: " + supplier.get());
        }
    }
    public static void regDoubleParseStaticPlaceholder(String placeholder, String value){
        SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, Text.of(value));
    }

    @Deprecated
    public static Text formatOld(String message) {
        return SeasonsAPI.getPlaceholderAPI().parseStringToText(message.replaceAll("&", "§"));
    }
    @Deprecated
    public static Text formatOld(String message, Map<String, String> placeholders) {
        var text =  SeasonsAPI.getPlaceholderAPI().parseStringToText(message.replaceAll("&", "§"));
        var placeholders0 = new HashMap<String, Text>();
        placeholders.forEach((key, value) -> {
            placeholders0.put(key, formatOld(value));
        });
        return SeasonsAPI.getPlaceholderAPI().parseText(text, placeholders0);
    }

}
