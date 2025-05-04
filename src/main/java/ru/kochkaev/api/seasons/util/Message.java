package ru.kochkaev.api.seasons.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.provider.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Message {

    @Deprecated
    public static void sendMessage2Server(String message){
        sendMessage2Server(Format.formatOld(message));
    }
    @Deprecated
    public static void sendMessage2Server(String message, Map<String, String> placeholders){
        sendMessage2Server(Format.formatOld(message, placeholders));
    }
    @Deprecated
    public static void sendMessage2ServerDefaultPlaceholders(String message){
        sendMessage2Server(Format.formatOld(message));
    }

    public static void sendMessage2Server(Text message){
        sendMessage2Server(message, new HashMap<>());
    }
    public static void sendMessage2Server(Text message, Map<String, Text> placeholders){
        sendMessage2Players(message, SeasonsAPI.getServer().getPlayerManager().getPlayerList().stream().map(it -> { return (PlayerEntity) it; }).toList(), placeholders);
    }

    @Deprecated
    public static void sendMessage2Players(String message, List<PlayerEntity> players){
        sendMessage2Players(Format.formatOld(message), players);
    }
    @Deprecated
    public static void sendMessage2Players(String message, List<PlayerEntity> players, Map<String, String> placeholders){
        sendMessage2Players(Format.formatOld(message, placeholders), players);
    }
    @Deprecated
    public static void sendMessage2PlayersDefaultPlaceholders(String message, List<PlayerEntity> players){
        sendMessage2Players(Format.formatOld(message), players);
    }

    public static void sendMessage2Players(Text message, List<PlayerEntity> players){
        sendMessage2Players(message, players, new HashMap<>());
    }
    public static void sendMessage2Players(Text message, List<PlayerEntity> players, Map<String, Text> placeholders){
        for (PlayerEntity player : players) sendMessage2Player(message, player,placeholders);
    }

    @Deprecated
    public static void sendMessage2Player(String message, PlayerEntity player){
        sendMessage2Player(Format.formatOld(message), player);
    }
    @Deprecated
    public static void sendMessage2Player(String message, PlayerEntity player, Map<String, String> placeholders){
        sendMessage2Player(Format.formatOld(message, placeholders), player);
    }
    @Deprecated
    public static void sendMessage2PlayerDefaultPlaceholders(String message, PlayerEntity player){
        sendMessage2Player(Format.formatOld(message), player);
    }

    public static void sendMessage2Player(Text message, PlayerEntity player){
        sendMessage2Player(message, player, new HashMap<>());
    }
    public static void sendMessage2Player(Text message, PlayerEntity player, Map<String, Text> placeholders){
        player.sendMessage(getFormattedMessage(message, placeholders), false);
    }

    @Deprecated
    public static String getFeedbackMessage(String message){
        return Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.chat.feedback").replace("%message%", message));
    }
    @Deprecated
    public static Text getFeedbackText(String message){
        return getFeedbackText(Text.of(message));
    }
    public static Text getFeedbackText(Text message){
        var placeholders = new HashMap<String, Text>();
        placeholders.put("message", message);
        return Format.formatTextMessage(Config.getModConfig("API").getConfig().getText("conf.format.chat.feedback"), placeholders);
    }

    @Deprecated
    public static String getFormattedMessage(String message, Map<String, String> placeholders) {
        var placeholders0 = new HashMap<String, Text>();
        placeholders.forEach((key, value) -> {
            placeholders0.put(key, Format.formatOld(value));
        });
        return getFormattedMessage(Format.formatOld(message, placeholders), placeholders0).getString();
    }

    public static Text getFormattedMessage(Text message, Map<String, Text> placeholders) {
        Map<String, Text> placeholders1 = new HashMap<>();
        placeholders1.put("message", Format.formatTextMessage(message, placeholders));
        placeholders1.put("seasons:display-name", Config.getModConfig("API").getLang().getText("lang.message.seasonsModDisplayName"));
        return Format.formatTextMessage(Config.getModConfig("API").getConfig().getText("conf.format.chat.message"), placeholders1);
    }

    @Deprecated
    public static Text getFormattedText(String message) {
        return getFormattedMessage(Format.formatOld(message), new HashMap<>());
    }

}
