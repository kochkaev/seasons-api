package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.predicate.entity.EntitySubPredicateTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.EntityList;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.*;

public class StrongCurrent extends EffectObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.strongCurrent.message.trigger");
        this.weathers = Collections.singletonList(Weather.STORMY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (player.getSteppingBlockState().getBlock() == Blocks.WATER) {
            if (player.hasVehicle()) {
                if (new Random().nextInt(100) <= 25) {
                    if ((Objects.requireNonNull(player.getVehicle()).getType() == EntityType.BOAT)) {
                        Entity boat = player.getVehicle();
                        player.dismountVehicle();
                        boat.setSwimming(true);
                    }
                    else player.dismountVehicle();
                }
            }
            else if (countOfInARowCalls == 1) sendMessage(player, Config.getLang().getString("lang.effect.strongCurrent.message.get"));
            else if (countOfInARowCalls == ticksPerAction) {
                giveEffect(player, StatusEffects.NAUSEA);
                giveEffect(player, StatusEffects.MINING_FATIGUE);
            }
            else if (countOfInARowCalls%ticksPerAction == 0) damage(player);
            return countOfInARowCalls + 1;
        }
        else if (player.getSteppingBlockState().getBlock() != Blocks.WATER) {
            if (countOfInARowCalls > 0) {
                removeEffect(player, StatusEffects.NAUSEA);
                return -1 - ticksPerAction;
            }
            else if (countOfInARowCalls == -1) {
                removeEffect(player, StatusEffects.MINING_FATIGUE);
                sendMessage(player, Config.getLang().getString("lang.effect.strongCurrent.message.remove"));
            }
        }
        return 0;
    }
}
