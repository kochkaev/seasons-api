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
//    private static final int ticksPerAction = ;
//    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new TickerTreadFactory());
//    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new TickerTreadFactory());
//    private static ScheduledFuture<?> task;
    private static final List<ServerPlayerEntity> playersRemoveList = new ArrayList<>();
    private static final List<ServerPlayerEntity> playersAddList = new ArrayList<>();
    private static final List<ChallengeObject> forceAllowed = new ArrayList<>();
    private static final List<ChallengeObject> forceDisabled = new ArrayList<>();

    private static final List<ChallengeObject> allowedChallenges = new ArrayList<>();
    private static boolean changeWeather = false;
//    private static boolean shutdown = false;

    public static void start() {
        isTicking = true;
//        task = executorService.scheduleAtFixedRate(ChallengesTicker::tick, 0, Config.getModConfig("API").getConfig().getInt("conf.tick.secondsPerTick"), TimeUnit.SECONDS);
    }

    public static void stop()  {
//        shutdown = true;
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
//    @SuppressWarnings("ResultOfMethodCallIgnored")
//    public static void close() {
//        task.cancel(true);
//        executorService.shutdown();
//        executorService.shutdownNow();
//        executorService.close();
//        try {
//            executorService.awaitTermination(1, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private static void shutdownTask() {
//
////        executorService.shutdown();
////        executorService.shutdownNow();
////        executorService = Executors.newSingleThreadScheduledExecutor(new TickerTreadFactory());
//    }

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

//    static class TickerTreadFactory implements ThreadFactory {
////        private static final AtomicInteger poolNumber = new AtomicInteger(1);
//        private final ThreadGroup group;
////        private final AtomicInteger threadNumber = new AtomicInteger(1);
//        private final String namePrefix;
//
//        @SuppressWarnings("removal")
//        TickerTreadFactory() {
//            SecurityManager s = System.getSecurityManager();
//            group = (s != null) ? s.getThreadGroup() :
//                    Thread.currentThread().getThreadGroup();
//            namePrefix = "Seasons Ticker";
//        }
//
//        public Thread newThread(Runnable r) {
//            Thread t = new Thread(group, r,
//                    namePrefix,
//                    0);
//            if (t.isDaemon())
//                t.setDaemon(false);
//            if (t.getPriority() != Thread.NORM_PRIORITY)
//                t.setPriority(Thread.NORM_PRIORITY);
//            return t;
//        }
//    }

    public static boolean isTicking() { return isTicking; }

}
