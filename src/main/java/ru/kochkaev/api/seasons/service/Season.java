package ru.kochkaev.api.seasons.service;

//import ru.kochkaev.seasons-api.Config.OldConfig;

import net.minecraft.server.PlayerManager;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.util.Message;

import java.util.*;

public class Season {

    private static SeasonObject CURRENT_SEASON;
    private static final List<SeasonObject> allSeasons = new ArrayList<>();

    public static void register(SeasonObject season) {
        allSeasons.add(season);
    }

    public static SeasonObject getCurrent(){
        return CURRENT_SEASON;
    }

    public static void setCurrent(SeasonObject season){
        CURRENT_SEASON = season;
        saveCurrentToConfig();
    }

    public static void onServerStartup(){
        String currentStr = Config.getCurrent("season");
        CURRENT_SEASON = (currentStr.equals("NONE") || currentStr.equals("example")) ? getRandomSeason() : getSeasonByID(currentStr);
        if (CURRENT_SEASON == null) CURRENT_SEASON = getSeasonByID("example");
    }

    public static void saveCurrentToConfig(){
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
    }

    public static void setSeason(SeasonObject season, PlayerManager players) {
        CURRENT_SEASON.onSeasonRemove();
        setCurrent(season);
        season.onSeasonSet();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%message%", season.getMessage());
        placeholders.put("%season%", season.getName());
        Message.sendMessage2Server(Config.getModConfig("API").getConfig().getString("conf.format.chat.message"), players, placeholders);
    }


    public static SeasonObject getSeasonByID(String id) {
        for (SeasonObject season : allSeasons) if (season.getId().equals(id)) return season;
        return null;
    }
    public static SeasonObject getRandomSeason() {
        return allSeasons.get(new Random().nextInt(allSeasons.size()));
    }

    public static List<SeasonObject> getAll() {
        return allSeasons;
    }}
