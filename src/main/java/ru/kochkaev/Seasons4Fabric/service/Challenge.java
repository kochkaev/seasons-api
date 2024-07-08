package ru.kochkaev.Seasons4Fabric.service;

import ru.kochkaev.Seasons4Fabric.ChallengesTicker;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;

import java.util.ArrayList;
import java.util.List;

public class Challenge {

    private static final List<ChallengeObject> challenges = new ArrayList<>();

    private static final List<ChallengeObject> challengesInCurrentWeather = new ArrayList<>();


    public static void register(Object... args) {
        ChallengeObject challenge = (ChallengeObject) args[0];
        challenge.register();
        challenges.add(challenge);
        if (ChallengesTicker.isTicking()) ChallengesTicker.addChallenge(challenge);
    }

    public static List<ChallengeObject> getChallenges() { return challenges; }

    public static List<ChallengeObject> getChallengesInCurrentWeather() { return challengesInCurrentWeather; }
    public static void updateChallengesInCurrentWeather() {
        challengesInCurrentWeather.clear();
        for (ChallengeObject challenge : challenges) if (challenge.isAllowed()) challengesInCurrentWeather.add(challenge);
    }

}
