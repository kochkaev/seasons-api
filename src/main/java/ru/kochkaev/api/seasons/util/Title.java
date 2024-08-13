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
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Title{


    public static void showActionBar(PlayerManager players) {
        Function<Text, Packet<?>> constructor = OverlayMessageS2CPacket::new;
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%season%", Season.getCurrent().getName());
        placeholders.put("%weather%", Weather.getCurrent().getName());
        Text title = Text.of(Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.title.actionbar"), placeholders));
        for (ServerPlayerEntity player : players.getPlayerList()) {
            try {
                player.networkHandler.sendPacket(constructor.apply(Texts.parse(null, title, player, 0)));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void showTitle(PlayerManager players) {
        Function<Text, Packet<?>> titleConstructor = TitleS2CPacket::new;
        Function<Text, Packet<?>> subtitleConstructor = SubtitleS2CPacket::new;
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%season%", Season.getCurrent().getName());
        placeholders.put("%weather%", Weather.getCurrent().getName());
        Map<String, String> placeholders1 = new HashMap<>();
        placeholders1.put("%messageNewDay%", Format.formatMessage(Config.getModConfig("API").getLang().getString("lang.message.messageNewDay"), placeholders));
        placeholders1.put("%info%", Format.formatMessage(Config.getModConfig("API").getLang().getString("lang.message.currentInfo"), placeholders));
        Text title = Text.of(Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.title.title"), placeholders1));
        Text subtitle = Text.of(Format.formatMessage(Config.getModConfig("API").getConfig().getString("conf.format.title.subtitle"), placeholders1));
        for (ServerPlayerEntity player : players.getPlayerList()) {
            try {
                player.networkHandler.sendPacket(titleConstructor.apply(Texts.parse(null, title, player, 0)));
                player.networkHandler.sendPacket(subtitleConstructor.apply(Texts.parse(null, subtitle, player, 0)));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
