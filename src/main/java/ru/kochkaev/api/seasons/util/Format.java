package ru.kochkaev.api.seasons.util;

import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.SeasonsAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Format {

    private static final Map<String, Supplier<String>> placeholders = new HashMap<>();

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
            for (String key : placeholders.keySet()) if (message.contains(key)) message = message.replaceAll(key, placeholders.get(key).get());
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
                tempMsg = tempMsg.replaceAll(placeholder, placeholders.get(placeholder).get());
            }
            message = Text.of(tempMsg);
        }

        // Colors
        message = Text.of(message.getString().replaceAll("&", "§"));

        return message;
    }
    private static String doubleParseFormat(String value){
        for (String key : placeholders.keySet()) if (value.contains(key)) value = value.replaceAll(key, placeholders.get(key).get());
        return value;
    }

    public static void regDynamicPlaceholder(String placeholder, Supplier<String> supplier){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().register(placeholder, SeasonsAPI.getPlaceholderAPI().getStringSupplier(supplier));
        else placeholders.put("%seasons:"+placeholder+"%", supplier);
    }
    public static void regStaticPlaceholder(String placeholder, String value){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().register(placeholder, value);
        else placeholders.put("%seasons:"+placeholder+"%", () -> value);
    }
    @SuppressWarnings("unchecked")
    public static void regDoubleParseDynamicPlaceholder(String placeholder, Supplier<?> supplier){
        switch (supplier.get()) {
            case String s -> {
                if (SeasonsAPI.getPlaceholderAPI() != null)
                    SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, () -> Text.of((String) supplier.get()));
                else placeholders.put("%seasons:" + placeholder + "%", () -> doubleParseFormat((String) supplier.get()));
            }
            case Text t -> {
                if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, SeasonsAPI.getPlaceholderAPI().getTextSupplier((Supplier<Text>) supplier));
                else placeholders.put("%seasons:"+placeholder+"%", () -> doubleParseFormat(((Text) supplier.get()).getString()));
            }
            default -> throw new IllegalStateException("Unexpected value: " + supplier.get());
        }
    }
    public static void regDoubleParseStaticPlaceholder(String placeholder, String value){
        if (SeasonsAPI.getPlaceholderAPI() != null) SeasonsAPI.getPlaceholderAPI().registerDoubleParse(placeholder, Text.of(value));
        else placeholders.put("%seasons:"+placeholder+"%", () -> doubleParseFormat(value));
    }

}
