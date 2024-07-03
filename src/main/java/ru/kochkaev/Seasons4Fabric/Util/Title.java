package ru.kochkaev.Seasons4Fabric.Util;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.Service.Season;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.function.Function;

public class Title{

    //static Title instance;

    //public Title(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions) {
    //    super(server, world, profile, clientOptions);
    //}

    public static void showActionBar(PlayerManager players) {
        Function<Text, Packet<?>> constructor = OverlayMessageS2CPacket::new;
        Text title = Text.of((Season.getCurrent().getName() + " &7• " + Weather.getCurrent().getName()).replaceAll("&", "§"));
        for (ServerPlayerEntity player : players.getPlayerList()) {
            //Main.getLogger().info(player.getName().getString());
            //player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.of((Season.getCurrent().getName() + " &7• " + Weather.getCurrent().getName()).replaceAll("&", "§"))));
            try {
                player.networkHandler.sendPacket(constructor.apply(Texts.parse(null, title, player, 0)));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //public static Title getInstance() { return instance; }

}
