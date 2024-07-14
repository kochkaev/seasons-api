package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.predicate.entity.EntitySubPredicateTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.IFuncRet;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Task;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.*;

public class StrongCurrent extends ChallengeObject {

    public StrongCurrent() {
        super(Config.getLang().getString("lang.effect.strongCurrent.message.trigger"), Collections.singletonList(Weather.STORMY), true);
    }

    private static final List<EntityType> boats = Arrays.asList(EntityType.BOAT, EntityType.CHEST_BOAT);

    @Override
    public void register() {
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (player.getSteppingBlockState().getBlock() == Blocks.WATER) {
            if (player.hasVehicle()) {
                if (new Random().nextInt(100) <= 5) {
                    if (boats.contains(Objects.requireNonNull(player.getVehicle()).getType())) {
                        Entity boat = player.getVehicle();
                        IFuncRet task = (args) -> {
                            Entity bt = (Entity) args.getFirst();
                            ServerPlayerEntity playr = (ServerPlayerEntity) args.get(1);
                            int counter = (Integer) args.get(2);
                            IFuncRet tsk = (IFuncRet) args.get(3);
                            bt.onBubbleColumnCollision(true);
                            bt.onBubbleColumnSurfaceCollision(true);
                            if (bt.shouldDismountUnderwater()) {
                                Task.removeTask(tsk);
                                playr.dismountVehicle();
                            }
                            return Arrays.asList(bt, playr, counter+1, tsk);
                        };
                        giveEffect(player, StatusEffects.NAUSEA);
                        Task.addTask(task, Arrays.asList(boat, player, 0, task));
                    }
                    else player.dismountVehicle();
                }
//                if (countOfInARowCalls > 0) {
//                    removeEffect(player, StatusEffects.NAUSEA);
//                    removeEffect(player, StatusEffects.MINING_FATIGUE);
//                    return 0;
//                }
            }
            else if (countOfInARowCalls == 1) sendMessage(player, Config.getLang().getString("lang.effect.strongCurrent.message.get"));
            else if (countOfInARowCalls == ticksPerAction) {
                giveEffect(player, StatusEffects.NAUSEA);
                giveEffect(player, StatusEffects.MINING_FATIGUE);
            }
            else if (countOfInARowCalls%ticksPerAction == 0) damageStorm(player);
            return countOfInARowCalls + 1;
        }
        else if (player.getSteppingBlockState().getBlock() != Blocks.WATER) {
            if (countOfInARowCalls > 0) {
                return -ticksPerAction-1;
            }
            else if (countOfInARowCalls == -1) {
                removeEffect(player, StatusEffects.NAUSEA);
                removeEffect(player, StatusEffects.MINING_FATIGUE);
                sendMessage(player, Config.getLang().getString("lang.effect.strongCurrent.message.remove"));
            }
            return countOfInARowCalls+1;
        }
        else if (countOfInARowCalls != 0) {
            return countOfInARowCalls+1;
        }
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {
        removeEffect(player, StatusEffects.MINING_FATIGUE);
        removeEffect(player, StatusEffects.NAUSEA);
    }
}
