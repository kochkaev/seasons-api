package ru.kochkaev.Seasons4Fabric;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Challenge;
import ru.kochkaev.Seasons4Fabric.service.Weather;
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

    private static final List<ChallengeObject> allowedChallenges = new ArrayList<>();
    private static boolean changeWeather = false;
    private static boolean shutdown = false;

    public static void start() {
        isTicking = true;
//        allowedChallenges.addAll(Challenge.getChallengesInCurrentWeather());
        executorService.scheduleAtFixedRate(ChallengesTicker::tick, 0, Config.getConfig().getInt("conf.tick.secondsPerTick"), TimeUnit.SECONDS);
    }

    public static void stop()  {
        isTicking = false;
        shutdown = true;
    }
    private static void shutdownTask() {
        for (ChallengeObject challenge : allowedChallenges) {
            for (ServerPlayerEntity player : players) {
                int count = countOfInARowCallsMap.get(player).get(challenge);
                if (count != 0) {
                    countOfInARowCallsMap.get(player).put(challenge, 0);
                    challenge.challengeEnd(player);
                }
            }
        }
        executorService.shutdown();
    }

    public static void tick() {
        for (ServerPlayerEntity player : players) {
            for (ChallengeObject challenge : allowedChallenges) {
                countOfInARowCallsMap.get(player).put(
                        challenge,
                        challenge.logic(player, countOfInARowCallsMap.get(player).get(challenge), ticksPerAction));
            }
        }
        if (!playersRemoveList.isEmpty()) removePlayersTask();
        if (!playersAddList.isEmpty()) addPlayersTask();
        if (changeWeather) changeWeatherTask();
        Main.getLogger().info("Challenges ticker is ticking");
        if (shutdown) shutdownTask();
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
            for (ChallengeObject challenge : allowedChallenges) {
                if (countOfInARowCallsMap.get(player).get(challenge) != 0) {
                    challenge.challengeEnd(player);
                }
            }
            countOfInARowCallsMap.remove(player);
            players.remove(player);
        }
        playersRemoveList.clear();
    }

    public static void addChallenge(ChallengeObject challenge) {
        for (ServerPlayerEntity player : players) countOfInARowCallsMap.get(player).put(challenge, 0);
        if (challenge.isAllowed()) allowedChallenges.add(challenge);
    }

    public static void changeWeather() {
        changeWeather = true;
    }
    private static void changeWeatherTask() {
        List<ChallengeObject> challenges = Challenge.getChallengesInCurrentWeather();
        for (ChallengeObject challenge : allowedChallenges) {
            if (!challenges.contains(challenge)) {
                for (ServerPlayerEntity player : players) {
                    int count = countOfInARowCallsMap.get(player).get(challenge);
                    if (count != 0) {
                        countOfInARowCallsMap.get(player).put(challenge, 0);
                        challenge.challengeEnd(player);
                    }
                }
            }
        }
        for (ChallengeObject challenge : challenges) if (!allowedChallenges.contains(challenge)) Message.sendMessage2Players(challenge.getTriggerMessage(), players);
        allowedChallenges.clear();
        allowedChallenges.addAll(challenges);
        changeWeather = false;
        Main.getLogger().info(allowedChallenges.toString());
    }

//    public static void setWorld(ServerWorld world){
//        ChallengesTicker.overworld = world;
//    }

    public static boolean isTicking() { return isTicking; }

}