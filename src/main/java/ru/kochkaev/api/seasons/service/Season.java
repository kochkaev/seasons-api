package ru.kochkaev.api.seasons.service;

//import ru.kochkaev.seasons-api.Config.OldConfig;

import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.SeasonsAPIServer;
import ru.kochkaev.api.seasons.object.SeasonObject;

import java.util.*;

public class Season {

    private static SeasonObject CURRENT_SEASON;
    private static final Map<String, SeasonObject> allSeasons = new HashMap<>();

    public static void register(SeasonObject season) {
        allSeasons.put(season.getId(), season);
    }

    public static SeasonObject getCurrent() {
        return CURRENT_SEASON;
    }

    public static void setCurrent(SeasonObject season) {
        CURRENT_SEASON = season;
        saveCurrentToConfig();
    }

    public static void onServerStartup() {
        String currentStr = Config.getCurrent("season");
        if (currentStr.equals("NONE") || currentStr.equals("example")) setCurrent(getRandomSeason());
        else CURRENT_SEASON = getSeasonByID(currentStr);
        if (CURRENT_SEASON == null) CURRENT_SEASON = getSeasonByID("example");
    }

    public static void saveCurrentToConfig() {
        String currentStr = CURRENT_SEASON.getId();
        Config.writeCurrent("season", currentStr);
        Config.saveCurrent();
    }

    public static void reloadFromConfig() {
        String currentStr = Config.getCurrent("season");
        SeasonObject season = getSeasonByID(currentStr);
        if (CURRENT_SEASON != season) {
            setSeason(season);
        }
        SeasonsAPI.getLogger().info("Seasons was reloaded!");
    }

    public static void reloadDynamics() {
        for (SeasonObject season : allSeasons.values()) {
            season.onReload();
        }
    }

    public static void setSeason(SeasonObject season) {
        CURRENT_SEASON.onSeasonRemove();
        setCurrent(season);
        season.onSeasonSet();
        SeasonsAPI.getLogger().info("Season was set to \"{}\"", season.getId());
    }


    public static SeasonObject getSeasonByID(String id) {
        return allSeasons.containsKey(id) ? allSeasons.get(id) : allSeasons.get("example");
    }

    public static SeasonObject getRandomSeason() {
        return allSeasons.size() > 1 ? allSeasons.entrySet().stream().filter(entry -> !entry.getKey().equals("example")).findAny().orElseThrow().getValue() : allSeasons.get("example");
    }

    public static List<SeasonObject> getAll() {
        return allSeasons.values().stream().toList();
    }
}
