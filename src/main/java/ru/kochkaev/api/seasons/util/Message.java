package ru.kochkaev.api.seasons.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

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
        String formattedMessage = MessageFormat.formatMessage(message);
        for (PlayerEntity player : players.getPlayerList()) player.sendMessage(Text.of(formattedMessage));
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

    public static void sendMessage2Players(String message, List<ServerPlayerEntity> players){
        String formattedMessage = MessageFormat.formatMessage(message);
        for (PlayerEntity player : players) player.sendMessage(Text.of(formattedMessage));
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

    public static void sendMessage2Player(String message, ServerPlayerEntity player){
        String formattedMessage = MessageFormat.formatMessage(message);
        player.sendMessage(Text.of(formattedMessage));
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

}
