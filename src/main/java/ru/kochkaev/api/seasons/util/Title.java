package ru.kochkaev.api.seasons.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Title{


    public static void showActionBar() {
        Function<Text, Packet<?>> constructor = OverlayMessageS2CPacket::new;
        Text title = Text.of(Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.title.actionbar")));
        for (ServerPlayerEntity player : SeasonsAPI.getServer().getPlayerManager().getPlayerList()) {
            try {
                player.networkHandler.sendPacket(constructor.apply(Texts.parse(null, title, player, 0)));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void showTitle() {
        Function<Text, Packet<?>> titleConstructor = TitleS2CPacket::new;
        Function<Text, Packet<?>> subtitleConstructor = SubtitleS2CPacket::new;
        Text title = Text.of(Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.title.title")));
        Text subtitle = Text.of(Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.title.subtitle")));
        for (ServerPlayerEntity player : SeasonsAPI.getServer().getPlayerManager().getPlayerList()) {
            try {
                player.networkHandler.sendPacket(titleConstructor.apply(Texts.parse(null, title, player, 0)));
                player.networkHandler.sendPacket(subtitleConstructor.apply(Texts.parse(null, subtitle, player, 0)));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
