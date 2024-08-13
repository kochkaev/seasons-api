package ru.kochkaev.api.seasons.service;

//import ru.kochkaev.seasons-api.Config.OldConfig;

import net.minecraft.server.PlayerManager;
import ru.kochkaev.api.seasons.Main;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.util.Message;

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
        CURRENT_SEASON = (currentStr.equals("NONE") || currentStr.equals("example")) ? getRandomSeason() : getSeasonByID(currentStr);
        if (CURRENT_SEASON == null) CURRENT_SEASON = getSeasonByID("example");
    }

    public static void saveCurrentToConfig() {
        String currentStr = CURRENT_SEASON.getId();
        Config.writeCurrent("season", currentStr);
        Config.saveCurrent();
    }

    public static void reloadFromConfig(PlayerManager players) {
        String currentStr = Config.getCurrent("season");
        SeasonObject season = getSeasonByID(currentStr);
        if (CURRENT_SEASON != season) {
            setSeason(season, players);
        }
        Main.getLogger().info("Seasons was reloaded!");
    }

    public static void setSeason(SeasonObject season, PlayerManager players) {
        CURRENT_SEASON.onSeasonRemove();
        setCurrent(season);
        season.onSeasonSet(players.getServer());
        Main.getLogger().info("Season was set to \"{}\"", season.getId());
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
