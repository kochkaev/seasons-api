package ru.kochkaev.Seasons4Fabric.Service;

//import ru.kochkaev.Seasons4Fabric.Config.OldConfig;

import ru.kochkaev.Seasons4Fabric.Config.Config;

public enum Season {

    WINTER(Config.getString("lang.season.winter.name"), Config.getString("lang.season.winter.message")),
    SPRING(Config.getString("lang.season.spring.name"), Config.getString("lang.season.spring.message")),
    SUMMER(Config.getString("lang.season.summer.name"), Config.getString("lang.season.summer.message")),
    FALL(Config.getString("lang.season.fall.name"), Config.getString("lang.season.fall.message"));

    private final String name;
    private final String message;

    Season(String name, String message){
        this.name = name;
        this.message = message;
    }

    private static Season CURRENT_SEASON;

    public static Season getCurrent(){
        return CURRENT_SEASON;
    }

    public static void setCurrent(Season season){
        CURRENT_SEASON = season;
    }

    public static void restoreCurrentFromConfig(){
        String currentStr = Config.getCurrent("season");
        CURRENT_SEASON = valueOf(currentStr);
    }
    public static void saveCurrentToConfig(){
        String currentStr = CURRENT_SEASON.toString();
        Config.writeCurrent("season", currentStr);
    }

    public String getName(){ return this.name; }
    public String getMessage(){ return this.message; }
}
