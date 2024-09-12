package ru.kochkaev.api.seasons.example;

import ru.kochkaev.api.seasons.object.SeasonObject;

import java.util.ArrayList;

public class ExampleSeason extends SeasonObject {
    public ExampleSeason() {
        super("example", () -> "No seasons matched", new ArrayList<>(), new ArrayList<>(), () -> 0);
    }

    @Override
    public void onSeasonSet() {
        sendMessage("No available seasons matched. Try to download additional mod based on Seasons API.");
    }

    @Override
    public void onSeasonRemove() {

    }
}
