package ru.kochkaev.Seasons4Fabric.Util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
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

    public static void sendNewMessage(String message){
        String formattedMessage = MessageFormat.formatMessage(message);
        MinecraftServer.getInstance().getPlayer().sendSystemMessage(new LiteralText(formattedMessage), Util.NIL_UUID);
        //ServerPlayerEntity.sendMessage(Text.of(formattedMessage));
    }

}
