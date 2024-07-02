package ru.kochkaev.Seasons4Fabric.Service;

import ru.kochkaev.Seasons4Fabric.Config.Config;

public enum Season {

    WINTER,
    SPRING,
    SUMMER,
    FALL;

    public static Season getCurrent(){
        return Season.valueOf(Config.CURRENT_SEASON);
    }
}
