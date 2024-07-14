package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class TheShivers extends ChallengeObject {

    public TheShivers() {
        super(Config.getLang().getString("lang.effect.theShivers.message.trigger"), Collections.singletonList(Weather.CHILLY), false);
    }

    @Override
    public void register() {
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (player.getSteppingBlockState().getBlock().equals(Blocks.WATER) && !player.hasVehicle()) {
            if (countOfInARowCalls == 0) sendMessage(player, Config.getLang().getString("lang.effect.theShivers.message.get"));
            if (countOfInARowCalls % ticksPerAction == 0) damageCold(player);
            return countOfInARowCalls+1;
        }
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {

    }
}
