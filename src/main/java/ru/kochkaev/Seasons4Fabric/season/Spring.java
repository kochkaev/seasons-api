package ru.kochkaev.Seasons4Fabric.season;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.SeasonObject;

public class Spring extends SeasonObject {

    public Spring() {
        super("SPRING", Config.getLang().getString("lang.season.spring.name"), Config.getLang().getString("lang.season.spring.message"));
    }

    @Override
    public void onSeasonSet() {

    }
    @Override
    public void onSeasonRemove() {

    }
}
