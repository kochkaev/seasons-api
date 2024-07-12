package ru.kochkaev.Seasons4Fabric;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Challenge;
import ru.kochkaev.Seasons4Fabric.util.Message;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChallengesTicker {

    //private static ServerWorld overworld;
    private static final Map<ServerPlayerEntity, Map<ChallengeObject, Integer>> countOfInARowCallsMap = new HashMap<>();
    private static final List<ServerPlayerEntity> players = new ArrayList<>();
    private static boolean isTicking = false;
    private static final int ticksPerAction = Config.getConfig().getInt("conf.tick.ticksPerAction");
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final List<ServerPlayerEntity> playersRemoveList = new ArrayList<>();
    private static final List<ServerPlayerEntity> playersAddList = new ArrayList<>();

    public static void start() {
        isTicking = true;
        executorService.scheduleAtFixedRate(ChallengesTicker::tick, 0, Config.getConfig().getInt("conf.tick.secondsPerTick"), TimeUnit.SECONDS);
    }

    public static void stop()  {
        isTicking = false;
        executorService.shutdown();
    }

    public static void tick() {
        for (ServerPlayerEntity player : players) {
            for (ChallengeObject challenge : Challenge.getChallengesInCurrentWeather()) countOfInARowCallsMap.get(player).put(challenge, challenge.logic(player, countOfInARowCallsMap.get(player).get(challenge), ticksPerAction));
        }
        if (!playersRemoveList.isEmpty()) removePlayersTask();
        if (!playersAddList.isEmpty()) addPlayersTask();
        Main.getLogger().info("Challenges ticker is ticking");
    }


    public static void addPlayer(ServerPlayerEntity player) {
        playersAddList.add(player);
    }
    public static void addPlayersTask() {
        for (ServerPlayerEntity player : playersAddList) {
            countOfInARowCallsMap.put(player, new HashMap<>());
            for (ChallengeObject effect : Challenge.getChallenges()) {
                countOfInARowCallsMap.get(player).put(effect, 0);
                //for (ChallengeObject challenge : Challenge.getChallengesInCurrentWeather()) Message.sendMessage2Player(challenge.getTriggerMessage(), player);
            }
            players.add(player);
        }
        playersAddList.clear();
    }

    public static void removePlayer(ServerPlayerEntity player) {
        playersRemoveList.add(player);
    }
    public static void removePlayersTask() {
        for (ServerPlayerEntity player : playersRemoveList) {
            countOfInARowCallsMap.remove(player);
            players.remove(player);
        }
        playersRemoveList.clear();
    }

    public static void addChallenge(ChallengeObject challenge) {
        for (ServerPlayerEntity player : players) countOfInARowCallsMap.get(player).put(challenge, 0);
    }

//    public static void setWorld(ServerWorld world){
//        ChallengesTicker.overworld = world;
//    }

    public static boolean isTicking() { return isTicking; }

}
