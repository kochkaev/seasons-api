package ru.kochkaev.api.seasons.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import ru.kochkaev.api.seasons.Main;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Title{

    //static Title instance;

    //public Title(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions) {
    //    super(server, world, profile, clientOptions);
    //}

    public static void showActionBar(PlayerManager players) {
        Function<Text, Packet<?>> constructor = OverlayMessageS2CPacket::new;
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%season%", Season.getCurrent().getName());
        placeholders.put("%weather%", Weather.getCurrent().getName());
        Text title = Text.of(MessageFormat.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.title.actionbar"), placeholders));
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
