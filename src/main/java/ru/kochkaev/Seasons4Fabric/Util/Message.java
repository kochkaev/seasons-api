package ru.kochkaev.Seasons4Fabric.Util;

import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ApiServices;
import net.minecraft.world.level.storage.LevelStorage;

import java.net.Proxy;

public abstract class Message extends MinecraftServer {

    static Message instance;

    public static Message getInstance(){
        return instance;
    }

    public Message(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory) {
        super(serverThread, session, dataPackManager, saveLoader, proxy, dataFixer, apiServices, worldGenerationProgressListenerFactory);
    }

    public void sendMessage(String message){
        String formattedMessage = MessageFormat.formatMessage(message);
        sendMessage(Text.literal(formattedMessage));
    }

}
