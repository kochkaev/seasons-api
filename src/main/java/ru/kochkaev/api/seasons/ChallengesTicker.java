package ru.kochkaev.api.seasons;

import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.ChallengeObject;
import ru.kochkaev.api.seasons.service.Challenge;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChallengesTicker {

    private static final Map<ServerPlayerEntity, Map<ChallengeObject, Integer>> countOfInARowCallsMap = new HashMap<>();
    private static final List<ServerPlayerEntity> players = new ArrayList<>();
    private static boolean isTicking = false;
    private static final int ticksPerAction = Config.getModConfig("API").getConfig().getInt("conf.tick.ticksPerAction");
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final List<ServerPlayerEntity> playersRemoveList = new ArrayList<>();
    private static final List<ServerPlayerEntity> playersAddList = new ArrayList<>();

    private static final List<ChallengeObject> allowedChallenges = new ArrayList<>();
    private static boolean changeWeather = false;
    private static boolean shutdown = false;

    public static void start() {
        isTicking = true;
        executorService.scheduleAtFixedRate(ChallengesTicker::tick, 0, Config.getModConfig("API").getConfig().getInt("conf.tick.secondsPerTick"), TimeUnit.SECONDS);
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
                    challenge.onChallengeEnd(player);
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
//        Main.getLogger().info("Challenges ticker is ticking");
        if (shutdown) shutdownTask();
    }


    public static void addPlayer(ServerPlayerEntity player) {
        playersAddList.add(player);
    }
    public static void addPlayersTask() {
        for (ServerPlayerEntity player : playersAddList) {
            players.add(player);
            countOfInARowCallsMap.put(player, new HashMap<>());
            for (ChallengeObject challenge : Challenge.getChallenges()) {
                countOfInARowCallsMap.get(player).put(challenge, 0);
                if (allowedChallenges.contains(challenge)) challenge.onChallengeStart(player);
            }
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
                    challenge.onChallengeEnd(player);
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
                        challenge.onChallengeEnd(player);
                    }
                }
            }
        }
        for (ChallengeObject challenge : challenges) if (!allowedChallenges.contains(challenge)) for (ServerPlayerEntity player : players) challenge.onChallengeStart(player);
        allowedChallenges.clear();
        allowedChallenges.addAll(challenges);
        changeWeather = false;
        SeasonsAPI.getLogger().info("In current weather available challenges: {}", allowedChallenges.stream().map(ChallengeObject::getID).toList());
    }

    public static boolean isTicking() { return isTicking; }

}
