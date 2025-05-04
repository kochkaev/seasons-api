package ru.kochkaev.api.seasons.example;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.provider.Season;

import java.util.Collections;

public class ExampleWeather extends WeatherObject {
    public ExampleWeather() {
        super(() -> Text.of("No weathers matched"), "example", false, false, 0, Collections.singletonList(Season.getSeasonByID("example")), null);
    }

    @Override
    public void onWeatherSet() {
        sendMessage(Text.of("No available weathers matched. Try to download additional mod based on Seasons API."));
    }

    @Override
    public void onWeatherRemove() {

    }
}
