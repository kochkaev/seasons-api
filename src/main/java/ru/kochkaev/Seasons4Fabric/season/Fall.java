package ru.kochkaev.Seasons4Fabric.season;

import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.SeasonObject;

public class Fall extends SeasonObject {

    public Fall() {
        super("FALL", Config.getLang().getString("lang.season.fall.name"), Config.getLang().getString("lang.season.fall.message"));
    }

    @Override
    public void onSeasonSet() {

    }
    @Override
    public void onSeasonRemove() {

    }
}
