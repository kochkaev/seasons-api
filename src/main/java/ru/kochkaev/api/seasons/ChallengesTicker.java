package ru.kochkaev.api.seasons;

import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.api.seasons.object.ChallengeObject;
import ru.kochkaev.api.seasons.provider.Challenge;
import ru.kochkaev.api.seasons.provider.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengesTicker {

    private static final Map<ServerPlayerEntity, Map<ChallengeObject, Integer>> countOfInARowCallsMap = new HashMap<>();
    private static final List<ServerPlayerEntity> players = new ArrayList<>();
    private static boolean isTicking = false;
    private static final List<ServerPlayerEntity> playersRemoveList = new ArrayList<>();
    private static final List<ServerPlayerEntity> playersAddList = new ArrayList<>();
    private static final List<ChallengeObject> forceAllowed = new ArrayList<>();
    private static final List<ChallengeObject> forceDisabled = new ArrayList<>();

    private static final List<ChallengeObject> allowedChallenges = new ArrayList<>();
    private static boolean changeWeather = false;

    public static void start() {
        isTicking = true;
    }

    public static void stop()  {
        for (ChallengeObject challenge : allowedChallenges) {
            for (ServerPlayerEntity player : players) {
                int count = countOfInARowCallsMap.get(player).get(challenge);
                if (count != 0) {
                    countOfInARowCallsMap.get(player).put(challenge, 0);
                    challenge.onChallengeEnd(player);
                }
            }
        }
        isTicking = false;
    }
    public static void tick() {
        if (isTicking){
            SeasonsAPI.getEvents().invokeBeforeChallengesTick(countOfInARowCallsMap, players, allowedChallenges);
            for (ServerPlayerEntity player : players) {
                for (ChallengeObject challenge : allowedChallenges) {
                    countOfInARowCallsMap.get(player).put(
                            challenge,
                            challenge.logic(player, countOfInARowCallsMap.get(player).get(challenge), Config.getModConfig("API").getConfig().getInt("conf.tick.challengeTicksPerAction")));
                }
            }
            if (!playersRemoveList.isEmpty()) removePlayersTask();
            if (!playersAddList.isEmpty()) addPlayersTask();
            if (!forceAllowed.isEmpty()) forceAllowTask();
            if (!forceDisabled.isEmpty()) forceDisableTask();
            if (changeWeather) changeWeatherTask();
//        Main.getLogger().info("Challenges ticker is ticking");
//            SeasonsAPI.getLogger().info("Tick!");
//            if (shutdown) shutdownTask();
            SeasonsAPI.getEvents().invokeAfterChallengesTick(countOfInARowCallsMap, players, allowedChallenges);
        }
    }


    public static void addPlayer(ServerPlayerEntity player) {
        if (player.isDead()) return;
        if (playersRemoveList.contains(player)) playersRemoveList.remove(player);
        else if (!players.contains(player)) playersAddList.add(player);
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
        if (playersAddList.contains(player)) playersAddList.remove(player);
        else if (players.contains(player)) playersRemoveList.add(player);
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
        SeasonsAPI.getLogger().debug("In current weather available challenges: " + allowedChallenges.stream().map(ChallengeObject::getID).toList());
    }

    public static List<ChallengeObject> getAllowedChallenges() {
        return allowedChallenges;
    }
    public static boolean isChallengeAllowed(ChallengeObject challenge) {
        return allowedChallenges.contains(challenge);
    }
    public static void forceAllowChallenge(ChallengeObject challenge) {
        forceAllowed.add(challenge);
    }
    public static void forceDisableChallenge(ChallengeObject challenge) {
        forceDisabled.add(challenge);
    }
    private static void forceAllowTask() {
        for (ChallengeObject challenge : forceAllowed) {
            for (ServerPlayerEntity player : players) {
                if (!allowedChallenges.contains(challenge)) {
                    challenge.onChallengeStart(player);
                    allowedChallenges.add(challenge);
                }
            }
        }
        forceAllowed.clear();
    }
    private static void forceDisableTask() {
        for (ChallengeObject challenge : forceDisabled) {
            for (ServerPlayerEntity player : players) {
                if (allowedChallenges.contains(challenge)) {
                    countOfInARowCallsMap.get(player).put(challenge, 0);
                    challenge.onChallengeEnd(player);
                    allowedChallenges.remove(challenge);
                }
            }
        }
        forceAllowed.clear();
    }

    public static boolean isTicking() { return isTicking; }

}
