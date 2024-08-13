package ru.kochkaev.api.seasons.service;

import ru.kochkaev.api.seasons.ChallengesTicker;
import ru.kochkaev.api.seasons.object.ChallengeObject;

import java.util.ArrayList;
import java.util.List;

public class Challenge {

    private static final List<ChallengeObject> challenges = new ArrayList<>();

    private static final List<ChallengeObject> challengesInCurrentWeather = new ArrayList<>();


    public static void register(ChallengeObject challenge) {
        challenge.register();
        challenges.add(challenge);
        if (ChallengesTicker.isTicking()) ChallengesTicker.addChallenge(challenge);
    }

    public static List<ChallengeObject> getChallenges() { return challenges; }
    public static ChallengeObject getChallengeByID(String id){
        return challenges.stream().filter(challenge -> challenge.getID().equals(id)).findFirst().orElse(null);
    }

    public static List<ChallengeObject> getChallengesInCurrentWeather() { return challengesInCurrentWeather; }
    public static void updateChallengesInCurrentWeather() {
        challengesInCurrentWeather.clear();
        for (ChallengeObject challenge : challenges) if (challenge.isAllowed()) challengesInCurrentWeather.add(challenge);
    }

}
