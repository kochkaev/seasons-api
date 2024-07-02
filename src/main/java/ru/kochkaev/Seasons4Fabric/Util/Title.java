package ru.kochkaev.Seasons4Fabric.Util;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import ru.kochkaev.Seasons4Fabric.Service.Season;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

public class Title extends ServerPlayerEntity{

    static Title instance;

    public Title(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions) {
        super(server, world, profile, clientOptions);
    }

    public void showSubtitle() {
        this.networkHandler.sendPacket(new SubtitleS2CPacket(Text.of((Season.getCurrent().getName() + " &7• "+ Weather.getCurrent().getName()).replaceAll("&", "§"))));
    }

    public static Title getInstance() { return instance; }

}
