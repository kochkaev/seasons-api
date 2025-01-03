package ru.kochkaev.api.seasons.API;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.api.seasons.object.ChallengeObject;

import java.util.List;
import java.util.Map;

public class SeasonsAPIFabricEvents extends Events {

    public static Event<APIInit> BEFORE_API_INIT = EventFactory.createArrayBacked(
            APIInit.class,
            (listeners) -> () -> {
                for (APIInit listener : listeners) {
                    listener.event();
                }
            }
    );
    public static Event<APIInit> AFTER_API_INIT = EventFactory.createArrayBacked(
            APIInit.class,
            (listeners) -> () -> {
                for (APIInit listener : listeners) {
                    listener.event();
                }
            }
    );
    public static Event<APIWorldInit> BEFORE_API_WORLD_INIT = EventFactory.createArrayBacked(
            APIWorldInit.class,
            (listeners) -> (server) -> {
                for (APIWorldInit listener : listeners) {
                    listener.event(server);
                }
            }
    );
    public static Event<APIWorldInit> AFTER_API_WORLD_INIT = EventFactory.createArrayBacked(
            APIWorldInit.class,
            (listeners) -> (server) -> {
                for (APIWorldInit listener : listeners) {
                    listener.event(server);
                }
            }
    );
    public static Event<APIInit> BEFORE_API_WORLD_CLOSE = EventFactory.createArrayBacked(
            APIInit.class,
            (listeners) -> () -> {
                for (APIInit listener : listeners) {
                    listener.event();
                }
            }
    );
    public static Event<APIInit> AFTER_API_WORLD_CLOSE = EventFactory.createArrayBacked(
            APIInit.class,
            (listeners) -> () -> {
                for (APIInit listener : listeners) {
                    listener.event();
                }
            }
    );
    public static Event<OnChallengesTick> BEFORE_CHALLENGES_TICK = EventFactory.createArrayBacked(
            OnChallengesTick.class,
            (listeners) -> (countOfInARowCallsMap, players, allowedChallenges) -> {
                for (OnChallengesTick listener : listeners) {
                    listener.tick(countOfInARowCallsMap, players, allowedChallenges);
                }
            }
    );
    public static Event<OnChallengesTick> AFTER_CHALLENGES_TICK = EventFactory.createArrayBacked(
            OnChallengesTick.class,
            (listeners) -> (countOfInARowCallsMap, players, allowedChallenges) -> {
                for (OnChallengesTick listener : listeners) {
                    listener.tick(countOfInARowCallsMap, players, allowedChallenges);
                }
            }
    );

    @Override
    public void invokeBeforeAPIInit() {
        BEFORE_API_INIT.invoker().event();
    }
    @Override
    public void invokeAfterAPIInit() {
        AFTER_API_INIT.invoker().event();
    }
    @Override
    public void invokeBeforeAPIWorldInit(MinecraftServer server) {
        BEFORE_API_WORLD_INIT.invoker().event(server);
    }
    @Override
    public void invokeAfterAPIWorldInit(MinecraftServer server) {
        AFTER_API_WORLD_INIT.invoker().event(server);
    }
    @Override
    public void invokeBeforeAPIWorldClose() {
        BEFORE_API_WORLD_CLOSE.invoker().event();
    }
    @Override
    public void invokeAfterAPIWorldClose() {
        AFTER_API_WORLD_CLOSE.invoker().event();
    }
    @Override
    public void invokeBeforeChallengesTick(Map<ServerPlayerEntity, Map<ChallengeObject, Integer>> countOfInARowCallsMap, List<ServerPlayerEntity> players, List<ChallengeObject> allowedChallenges) {
        BEFORE_CHALLENGES_TICK.invoker().tick(countOfInARowCallsMap, players, allowedChallenges);
    }
    @Override
    public void invokeAfterChallengesTick(Map<ServerPlayerEntity, Map<ChallengeObject, Integer>> countOfInARowCallsMap, List<ServerPlayerEntity> players, List<ChallengeObject> allowedChallenges) {
        AFTER_CHALLENGES_TICK.invoker().tick(countOfInARowCallsMap, players, allowedChallenges);
    }

    @Override
    public void registerBeforeAPIInit(APIInit listener) {
        BEFORE_API_INIT.register(listener);
    }
    @Override
    public void registerAfterAPIInit(APIInit listener) {
        AFTER_API_INIT.register(listener);
    }
    @Override
    public void registerBeforeAPIWorldInit(APIWorldInit listener) {
        BEFORE_API_WORLD_INIT.register(listener);
    }
    @Override
    public void registerAfterAPIWorldInit(APIWorldInit listener) {
        AFTER_API_WORLD_INIT.register(listener);
    }
    @Override
    public void registerBeforeAPIWorldClose(APIInit listener) {
        BEFORE_API_WORLD_CLOSE.register(listener);
    }
    @Override
    public void registerAfterAPIWorldClose(APIInit listener) {
        AFTER_API_WORLD_CLOSE.register(listener);
    }
    @Override
    public void registerBeforeChallengesTick(OnChallengesTick listener) {
        BEFORE_CHALLENGES_TICK.register(listener);
    }
    @Override
    public void registerAfterChallengesTick(OnChallengesTick listener) {
        AFTER_CHALLENGES_TICK.register(listener);
    }
}
