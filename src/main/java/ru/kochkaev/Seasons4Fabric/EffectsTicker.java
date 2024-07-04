package ru.kochkaev.Seasons4Fabric;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EffectsTicker {

    private static List<EffectObject> effects = List.of();
    private static ServerWorld overworld;
    private static Map<ServerPlayerEntity, Map<EffectObject, Integer>> countOfInARowCallsMap;

    EffectsTicker() {
        for (ServerPlayerEntity player : overworld.getPlayers()) {
            for (EffectObject effect : effects) {
                Map<EffectObject, Integer> temp = new HashMap<>();
                temp.put(effect, 0);
                countOfInARowCallsMap.put(player, temp);
            }
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(EffectsTicker::tickPerSecond, 0, 1, TimeUnit.SECONDS);
    }

    public static void tickPerSecond() {
        for (ServerPlayerEntity player : overworld.getPlayers()) {
            if (player.getServerWorld() == overworld) for (EffectObject effect : effects) if (effect.isAllowed()) effect.logic(player, countOfInARowCallsMap.get(player).get(effect));
        }
    }

    public static void add(EffectObject effect) {
        effects.add(effect);
    }
    public static void setWorld(ServerWorld world){
        EffectsTicker.overworld = world;
    }

    public static List<EffectObject> getListOfEffects() { return effects; }

}
