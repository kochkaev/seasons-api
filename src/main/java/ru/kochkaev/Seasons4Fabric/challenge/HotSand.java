package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HotSand extends ChallengeObject {

    private static final List<Block> hots = Arrays.asList(Blocks.SAND, Blocks.RED_SAND);

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.hotSand.message.trigger");
        this.weathers = Collections.singletonList(Weather.SCORCHING);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (!player.hasVehicle() && hots.contains(player.getSteppingBlockState().getBlock())) {
            sendMessage(player, Config.getLang().getString("lang.effect.hotSand.message.get"));
            if (countOfInARowCalls>ticksPerAction) damage(player);
            return countOfInARowCalls+1;
        }
        return 0;
    }
}
