package ru.kochkaev.api.seasons.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.provider.Config;

import java.util.function.Function;
import java.util.function.Predicate;

public class Title{


    public static void showActionBar() {
        Function<Text, Packet<?>> constructor = OverlayMessageS2CPacket::new;
        Text title = Format.formatTextMessage(Config.getModConfig("API").getConfig().getText("conf.format.title.actionbar"));
        final var enabledFor = Config.getCurrent("players_show_actionbar");
        final var inverse = Config.getModConfig("API").getConfig().getBoolean("conf.enable.title.actionbarDefaultForAll");
//        final Predicate<? super ServerPlayerEntity> condition = (ServerPlayerEntity player) -> {
//            return inverse != enabledFor.contains(player.getNameForScoreboard());
//        };
        final var availablePlayers = SeasonsAPI.getServer().getPlayerManager().getPlayerList().stream().filter(it -> inverse != enabledFor.contains(it.getNameForScoreboard())).toList();
        for (ServerPlayerEntity player : availablePlayers) {
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
        Text title = Format.formatTextMessage(Config.getModConfig("API").getConfig().getText("conf.format.title.title"));
        Text subtitle = Format.formatTextMessage(Config.getModConfig("API").getConfig().getText("conf.format.title.subtitle"));
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
