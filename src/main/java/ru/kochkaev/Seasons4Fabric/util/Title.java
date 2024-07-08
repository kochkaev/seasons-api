package ru.kochkaev.Seasons4Fabric.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import ru.kochkaev.Seasons4Fabric.service.Season;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.function.Function;

public class Title{

    //static Title instance;

    //public Title(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions) {
    //    super(server, world, profile, clientOptions);
    //}

    public static void showActionBar(PlayerManager players) {
        Function<Text, Packet<?>> constructor = OverlayMessageS2CPacket::new;
        Weather weather = Weather.isNight() ? Weather.NIGHT : Weather.getCurrent();
        Text title = Text.of((Season.getCurrent().getName() + " &7• " + weather.getName()).replaceAll("&", "§"));
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
