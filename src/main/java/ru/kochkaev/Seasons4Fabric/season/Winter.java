package ru.kochkaev.Seasons4Fabric.season;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.SeasonObject;

public class Winter extends SeasonObject {

    public Winter() {
        super("WINTER", Config.getLang().getString("lang.season.winter.name"), Config.getLang().getString("lang.season.winter.message"));
    }

    @Override
    public void onSeasonSet() {

    }
    @Override
    public void onSeasonRemove() {

    }
}
