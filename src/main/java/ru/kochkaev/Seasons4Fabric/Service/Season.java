package ru.kochkaev.Seasons4Fabric.Service;

//import ru.kochkaev.Seasons4Fabric.Config.OldConfig;

import net.minecraft.server.PlayerManager;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Util.Message;

public enum Season {

    WINTER("lang.season.winter.name", "lang.season.winter.message"),
    SPRING("lang.season.spring.name", "lang.season.spring.message"),
    SUMMER("lang.season.summer.name", "lang.season.summer.message"),
    FALL("lang.season.fall.name", "lang.season.fall.message");

    private final String nameKey;
    private final String messageKey;

    Season(String nameKey, String messageKey){
        this.nameKey = nameKey;
        this.messageKey = messageKey;
    }

    private static Season CURRENT_SEASON;

    public static Season getCurrent(){
        return CURRENT_SEASON;
    }

    public static void setCurrent(Season season){
        CURRENT_SEASON = season;
        saveCurrentToConfig();
    }

    public static void restoreCurrentFromConfig(){
        String currentStr = Config.getCurrent("season");
        CURRENT_SEASON = valueOf(currentStr);
    }
    public static void saveCurrentToConfig(){
        String currentStr = CURRENT_SEASON.toString();
        Config.writeCurrent("season", currentStr);
        Config.saveCurrent();
    }

    public static void reloadFromConfig(PlayerManager players) {
        String currentStr = Config.getCurrent("season");
        if (CURRENT_SEASON != valueOf(currentStr)) {
            setSeason(valueOf(currentStr), players);
        }
    }

    public static void setSeason(Season season, PlayerManager players) {
        setCurrent(season);
        Message.sendNewMessage(season.getMessage(), players);
    }

    public String getName(){ return Config.getString(this.nameKey); }
    public String getMessage(){ return Config.getString(this.messageKey); }

    public static Season getSeasonByID(String id) { return valueOf(id); }

    public static Season[] getAll() {
        return values();
    }}
