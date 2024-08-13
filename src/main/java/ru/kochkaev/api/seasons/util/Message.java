package ru.kochkaev.api.seasons.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.config.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Message {

    public static void sendMessage2Server(String message, PlayerManager players){
        sendMessage2Server(message, players, new HashMap<>());
    }
    public static void sendMessage2Server(String message, PlayerManager players, Map<String, String> placeholders){
        String formattedMessage = getFormattedMessage(message, placeholders);
        for (PlayerEntity player : players.getPlayerList()) player.sendMessage(Text.of(formattedMessage));
    }

    public static void sendMessage2Players(String message, List<ServerPlayerEntity> players){
        sendMessage2Players(message, players, new HashMap<>());
    }
    public static void sendMessage2Players(String message, List<ServerPlayerEntity> players, Map<String, String> placeholders){
        String formattedMessage = getFormattedMessage(message, placeholders);
        for (PlayerEntity player : players) player.sendMessage(Text.of(formattedMessage));
    }

    public static void sendMessage2Player(String message, ServerPlayerEntity player){
        sendMessage2Player(message, player, new HashMap<>());
    }
    public static void sendMessage2Player(String message, ServerPlayerEntity player, Map<String, String> placeholders){
        player.sendMessage(Text.of(getFormattedMessage(message, placeholders)));
    }

    public static String getFeedbackMessage(String message){
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%message%", Format.formatMessage(message, new HashMap<>()));
        return Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.chat.feedback"), placeholders);
    }
    public static Text getFeedbackText(String message){
        return Text.of(getFeedbackMessage(message));
    }

    public static String getFormattedMessage(String message, Map<String, String> placeholders) {
        Map<String, String> placeholders1 = new HashMap<>();
        placeholders1.put("%message%", Format.formatMessage(message, placeholders));
        placeholders1.put("%seasonsModDisplayName%", Config.getModConfig("API").getLang().getString("lang.message.seasonsModDisplayName"));
        return Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.chat.message"), placeholders1);
    }

}
