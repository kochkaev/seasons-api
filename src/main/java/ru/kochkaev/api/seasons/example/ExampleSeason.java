package ru.kochkaev.api.seasons.example;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.object.SeasonObject;

import java.util.ArrayList;

public class ExampleSeason extends SeasonObject {
    public ExampleSeason() {
        super(() -> Text.of("No seasons matched"), "example", new ArrayList<>(), new ArrayList<>(), () -> 0);
    }

    @Override
    public void onSeasonSet() {
        sendMessage(Text.of("No available seasons matched. Try to download additional mod based on Seasons API."));
    }

    @Override
    public void onSeasonRemove() {

    }
}
