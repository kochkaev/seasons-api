package ru.kochkaev.api.seasons.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.config.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Message {
    //public Message(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions) {
    //    super(server, world, profile, clientOptions);
    //}

    //static Message instance;

    //public static Message getInstance(){
    //    return instance;
    //}

    //public Message(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory) {
    //    super(serverThread, session, dataPackManager, saveLoader, proxy, dataFixer, apiServices, worldGenerationProgressListenerFactory);
    //}

    public static void sendMessage2Server(String message, PlayerManager players){
        sendMessage2Server(message, players, new HashMap<>());
    }
    public static void sendMessage2Server(String message, PlayerManager players, Map<String, String> map){
        map.put("%seasonsModDisplayName%", Config.getModConfig("API").getLang().getString("lang.message.seasonsModDisplayName"));
        String formattedMessage = MessageFormat.formatMessage(message, map);
        for (PlayerEntity player : players.getPlayerList()) player.sendMessage(Text.of(formattedMessage));
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

    public static void sendMessage2Players(String message, List<ServerPlayerEntity> players){
        sendMessage2Players(message, players, new HashMap<>());
    }
    public static void sendMessage2Players(String message, List<ServerPlayerEntity> players, Map<String, String> map){
        map.put("%seasonsModDisplayName%", Config.getModConfig("API").getLang().getString("lang.message.seasonsModDisplayName"));
        String formattedMessage = MessageFormat.formatMessage(message, map);
        for (PlayerEntity player : players) player.sendMessage(Text.of(formattedMessage));
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

    public static void sendMessage2Player(String message, ServerPlayerEntity player){
        sendMessage2Player(message, player, new HashMap<>());
    }
    public static void sendMessage2Player(String message, ServerPlayerEntity player, Map<String, String> map){
        map.put("%seasonsModDisplayName%", Config.getModConfig("API").getLang().getString("lang.message.seasonsModDisplayName"));
        String formattedMessage = MessageFormat.formatMessage(message, map);
        player.sendMessage(Text.of(formattedMessage));
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

}
