package ru.kochkaev.Seasons4Fabric.season;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.SeasonObject;

public class Summer extends SeasonObject {

    public Summer() {
        super("SUMMER", Config.getLang().getString("lang.season.summer.name"), Config.getLang().getString("lang.season.summer.message"));
    }

    @Override
    public void onSeasonSet() {

    }
    @Override
    public void onSeasonRemove() {

    }
}
