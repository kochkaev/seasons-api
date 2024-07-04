package ru.kochkaev.Seasons4Fabric.Util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.net.Proxy;

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

    public static void sendMessage2Player(String message, ServerPlayerEntity player){
        String formattedMessage = MessageFormat.formatMessage(message);
        player.sendMessage(Text.of(formattedMessage));
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

}
