package ru.kochkaev.api.seasons.example;

import net.minecraft.server.MinecraftServer;
import ru.kochkaev.api.seasons.object.SeasonObject;

public class ExampleSeason extends SeasonObject {
    public ExampleSeason() {
        super("example", () -> "No seasons matched");
    }

    @Override
    public void onSeasonSet() {
        sendMessage("No available seasons matched. Try to download additional mod based on Seasons API.");
    }

    @Override
    public void onSeasonRemove() {

    }
}
