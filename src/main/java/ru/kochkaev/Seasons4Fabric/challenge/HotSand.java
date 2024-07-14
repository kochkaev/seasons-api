package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HotSand extends ChallengeObject {

    public HotSand() {
        super(Config.getLang().getString("lang.effect.hotSand.message.trigger"), Collections.singletonList(Weather.SCORCHING), false);
    }

    private static final List<Block> hots = Arrays.asList(Blocks.SAND, Blocks.RED_SAND);

    @Override
    public void register() {
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (!player.hasVehicle() && hots.contains(player.getSteppingBlockState().getBlock()) && !player.isSneaking()) {
            sendMessage(player, Config.getLang().getString("lang.effect.hotSand.message.get"));
            spawnParticles(player, ParticleTypes.SMALL_FLAME, false, 0, 10);
            damageHot(player);
            return countOfInARowCalls+1;
        }
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {

    }
}
