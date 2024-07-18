package ru.kochkaev.api.seasons.config;

import ru.kochkaev.api.seasons.object.TXTConfigObject;

public class DefaultTXTConfig extends TXTConfigObject{

    public DefaultTXTConfig() {
        super("", "api-config", "config");
        generate();
    }

    private void generate() {

        String copyright = Config.getCopyright() + "#\n# It's mod config!\n#" +
                "\n# ** CHANCE =  chance of this weather coming on a new day (less than 100)\n#" +
                "\n# Other config files:\n#   - current.json - information about the current Weather/Season,\n#     is updated when the server is turned off.\n#   - lang - directory, contains translations of mod names/messages.\n\n\n";
        addString(copyright);

        /// Chances of weather
        addVoid();
        addComment("* WEATHER CHANCE");
        // Night
        addComment("Night");
        addValueAndCommentDefault("conf.weather.night.chance", "1");
        // Snowy
        addComment("Snowy");
        addValueAndCommentDefault("conf.weather.snowy.chance", "15");
        // Freezing
        addComment("Freezing");
        addValueAndCommentDefault("conf.weather.freezing.chance", "15");
        // Stormy
        addComment("Stormy");
        addValueAndCommentDefault("conf.weather.stormy.chance", "10");
        // Cold
        addComment("Cold");
        addValueAndCommentDefault("conf.weather.cold.chance", "40");
        // Warm
        addComment("Warm");
        addValueAndCommentDefault("conf.weather.warm.chance", "25");
        // Hot
        addComment("Hot");
        addValueAndCommentDefault("conf.weather.hot.chance", "20");
        // Scorching
        addComment("Scorching");
        addValueAndCommentDefault("conf.weather.scorching.chance", "10");
        // Rainy
        addComment("Rainy");
        addValueAndCommentDefault("conf.weather.rainy.chance", "10");
        // Chilly
        addComment("Chilly");
        addValueAndCommentDefault("conf.weather.chilly.chance", "15");
        // Breezy
        addComment("Breezy");
        addValueAndCommentDefault("conf.weather.breezy.chance", "15");
        // Beautiful
        addComment("Beautiful");
        addValueAndCommentDefault("conf.weather.beautiful.chance", "20");

        // Settings
        addVoid();
        addComment("* SETTINGS");
        addComment("Tick of day starts");
        addValueAndCommentDefault("conf.tick.day.start", "0");
        addComment("Tick of day ends");
        addValueAndCommentDefault("conf.tick.day.end", "12542");
        addComment("Seconds per seasons-api tick");
        addValueAndCommentDefault("conf.tick.secondsPerTick", "1");
        addComment("Ticks before action starts (for example, damage)");
        addValueAndCommentDefault("conf.tick.ticksPerAction", "3");

    }
}
