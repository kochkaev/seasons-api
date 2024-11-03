package ru.kochkaev.api.seasons.API;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.api.seasons.object.ChallengeObject;

import java.util.List;
import java.util.Map;

public abstract class Events {

    public abstract void invokeBeforeAPIInit();
    public abstract void invokeAfterAPIInit();
    public abstract void invokeBeforeAPIWorldInit(MinecraftServer server);
    public abstract void invokeAfterAPIWorldInit(MinecraftServer server);
    public abstract void invokeBeforeChallengesTick(
            Map<ServerPlayerEntity, Map<ChallengeObject, Integer>>countOfInARowCallsMap,
            List<ServerPlayerEntity> players,
            List<ChallengeObject> allowedChallenges
    );
    public abstract void invokeAfterChallengesTick(
            Map<ServerPlayerEntity, Map<ChallengeObject, Integer>> countOfInARowCallsMap,
            List<ServerPlayerEntity> players,
            List<ChallengeObject> allowedChallenges
    );

    public abstract void registerBeforeAPIInit(APIInit listener);
    public abstract void registerAfterAPIInit(APIInit listener);
    public abstract void registerBeforeAPIWorldInit(APIWorldInit listener);
    public abstract void registerAfterAPIWorldInit(APIWorldInit listener);
    public abstract void registerBeforeChallengesTick(OnChallengesTick listener);
    public abstract void registerAfterChallengesTick(OnChallengesTick listener);

    @FunctionalInterface
    public interface APIInit {
        void event();
    }
    @FunctionalInterface
    public interface APIWorldInit {
        void event(MinecraftServer server);
    }
    @FunctionalInterface
    public interface OnChallengesTick {
        void tick(Map<ServerPlayerEntity, Map<ChallengeObject, Integer>> countOfInARowCallsMap,
                  List<ServerPlayerEntity> players,
                  List<ChallengeObject> allowedChallenges
        );
    }

}
