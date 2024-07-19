package ru.kochkaev.api.seasons.example;

import ru.kochkaev.api.seasons.object.SeasonObject;

public class ExampleSeason extends SeasonObject {
    public ExampleSeason() {
        super("example", "No seasons matched", "No available seasons matched. Try to download additional mod based on Seasons API.");
    }

    @Override
    public void onSeasonSet() {

    }

    @Override
    public void onSeasonRemove() {

    }
}
