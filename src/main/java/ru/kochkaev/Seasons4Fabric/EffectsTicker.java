package ru.kochkaev.Seasons4Fabric;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Effect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EffectsTicker {

    private static ServerWorld overworld;
    private static final Map<ServerPlayerEntity, Map<EffectObject, Integer>> countOfInARowCallsMap = new HashMap<>();
    private static boolean isTicking = false;

    public static void start() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        isTicking = true;
        executorService.scheduleAtFixedRate(EffectsTicker::tickPerSecond, 0, 1, TimeUnit.SECONDS);
    }

    public static void tickPerSecond() {
        for (ServerPlayerEntity player : overworld.getPlayers()) {
            if (player.getServerWorld() == overworld) for (EffectObject effect : Effect.getEffectsInCurrentWeather()) countOfInARowCallsMap.get(player).put(effect, effect.logic(player, countOfInARowCallsMap.get(player).get(effect)));
        }
    }


    public static void addPlayer(ServerPlayerEntity player) {
        for (EffectObject effect : Effect.getEffects()) {
            countOfInARowCallsMap.put(player, new HashMap<>());
            countOfInARowCallsMap.get(player).put(effect, 0);
        }
    }

    public static void removePlayer(ServerPlayerEntity player) {
        countOfInARowCallsMap.remove(player);
    }

    public static void addEffect(EffectObject effect) {
        for (ServerPlayerEntity player : overworld.getPlayers()) countOfInARowCallsMap.get(player).put(effect, 0);
    }

    public static void setWorld(ServerWorld world){
        EffectsTicker.overworld = world;
    }

    public static boolean isTicking() { return isTicking; }

}
