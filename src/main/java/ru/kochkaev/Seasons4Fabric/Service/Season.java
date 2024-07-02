package ru.kochkaev.Seasons4Fabric.Service;

import ru.kochkaev.Seasons4Fabric.Config.Config;

public enum Season {

    WINTER(Config.LANG_SEASON_WINTER_NAME, Config.LANG_SEASON_WINTER_MESSAGE),
    SPRING(Config.LANG_SEASON_SPRING_NAME, Config.LANG_SEASON_SPRING_MESSAGE),
    SUMMER(Config.LANG_SEASON_SUMMER_NAME, Config.LANG_SEASON_SUMMER_MESSAGE),
    FALL(Config.LANG_SEASON_FALL_NAME, Config.LANG_SEASON_FALL_MESSAGE);

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
        String currentStr = Config.getCurrentWeather();
        CURRENT_SEASON = valueOf(currentStr);
    }
    public static void saveCurrentToConfig(){
        String currentStr = CURRENT_SEASON.toString();
        Config.setCurrentWeather(currentStr);
    }
}
