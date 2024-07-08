package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class TheShivers extends EffectObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.theShivers.message.trigger");
        this.weathers = Collections.singletonList(Weather.CHILLY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (player.getSteppingBlockState().getBlock() == Blocks.WATER && !player.hasVehicle()) {
            if (countOfInARowCalls == 0) sendMessage(player, Config.getLang().getString("lang.effect.theShivers.message.get"));
            if (countOfInARowCalls % ticksPerAction == 0) damage(player);
            return countOfInARowCalls+1;
        }
        return 0;
    }
}
