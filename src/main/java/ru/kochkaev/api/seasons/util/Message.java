package ru.kochkaev.api.seasons.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.SeasonsAPIServer;
import ru.kochkaev.api.seasons.service.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Message {

    public static void sendMessage2Server(String message){
        sendMessage2Server(message, new HashMap<>());
    }
    public static void sendMessage2Server(String message, Map<String, String> placeholders){
        String formattedMessage = getFormattedMessage(message, placeholders);
        for (PlayerEntity player : SeasonsAPIServer.getServer().getPlayerManager().getPlayerList()) player.sendMessage(Text.of(formattedMessage));
    }
    public static void sendMessage2ServerDefaultPlaceholders(String message){
        Text formattedText = getFormattedText(message);
        for (PlayerEntity player : SeasonsAPIServer.getServer().getPlayerManager().getPlayerList()) player.sendMessage(formattedText);
    }

    public static void sendMessage2Players(String message, List<PlayerEntity> players){
        sendMessage2Players(message, players, new HashMap<>());
    }
    public static void sendMessage2Players(String message, List<PlayerEntity> players, Map<String, String> placeholders){
        String formattedMessage = getFormattedMessage(message, placeholders);
        for (PlayerEntity player : players) player.sendMessage(Text.of(formattedMessage));
    }
    public static void sendMessage2PlayersDefaultPlaceholders(String message, List<PlayerEntity> players){
        Text formattedText = getFormattedText(message);
        for (PlayerEntity player : players) player.sendMessage(formattedText);
    }

    public static void sendMessage2Player(String message, PlayerEntity player){
        sendMessage2Player(message, player, new HashMap<>());
    }
    public static void sendMessage2Player(String message, PlayerEntity player, Map<String, String> placeholders){
        player.sendMessage(Text.of(getFormattedMessage(message, placeholders)));
    }
    public static void sendMessage2PlayerDefaultPlaceholders(String message, PlayerEntity player){
        player.sendMessage(getFormattedText(message));
    }

    public static String getFeedbackMessage(String message){
        return Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.chat.feedback").replace("%message%", message));
    }
    public static Text getFeedbackText(String message){
        return Format.formatTextMessage(Text.of(Config.getModConfig("API").getConfig().getString("conf.format.chat.feedback").replace("%message%", message)));
    }

    public static String getFormattedMessage(String message, Map<String, String> placeholders) {
        Map<String, String> placeholders1 = new HashMap<>();
        placeholders1.put("%message%", Format.formatMessage(message, placeholders));
        placeholders1.put("%seasons:display-name%", Config.getModConfig("API").getLang().getString("lang.message.seasonsModDisplayName"));
        return Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.chat.message"), placeholders1);
    }

    public static Text getFormattedText(String message) {
        return Format.formatTextMessage(Text.of(Config.getModConfig("API").getConfig().getString("conf.format.chat.message").replace("%message%", message)));
    }

}
