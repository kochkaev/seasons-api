package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WetMud extends ChallengeObject {

    private final static List<Block> muddy = Arrays.asList(Blocks.MUD, Blocks.DIRT, Blocks.DIRT_PATH, Blocks.FARMLAND);

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.wetMud.message.trigger");
        this.weathers = Collections.singletonList(Weather.RAINY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (muddy.contains(player.getSteppingBlockState().getBlock()) && !player.hasVehicle()) {
            giveEffect(player, StatusEffects.SLOWNESS);
            sendMessage(player, Config.getLang().getString("lang.effect.wetMud.message.get"));
            return countOfInARowCalls+1;
        }
        else if (countOfInARowCalls>0) {
            removeEffect(player, StatusEffects.SLOWNESS);
        }
        return 0;
    }
}
