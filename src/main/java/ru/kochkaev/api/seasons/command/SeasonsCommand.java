package ru.kochkaev.api.seasons.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.Message;

public class SeasonsCommand {

    public SeasonsCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("seasons")
                .then(CommandManager.literal("set")
                        .then(CommandManager.literal("season")
                                .then(CommandManager.argument("season", StringArgumentType.string()).suggests(new SeasonSuggestionProvider()).executes(context -> setSeason(StringArgumentType.getString(context, "season"), context.getSource()))))
                        .then(CommandManager.literal("weather")
                                .then(CommandManager.argument("weather", StringArgumentType.string()).suggests(new WeatherSuggestionProvider()).executes(context -> setWeather(StringArgumentType.getString(context, "weather"), context.getSource()))))
                        .then(CommandManager.literal("lang")
                                .then(CommandManager.argument("lang", StringArgumentType.string()).suggests(new LangSuggestionProvider()).executes(context -> setLang(StringArgumentType.getString(context, "lang"), context.getSource())))))
                .then(CommandManager.literal("reload").executes(context -> reload(context.getSource())))
        );
    }

    public static int setSeason(String seasonID, ServerCommandSource source) {
        Season.setSeason(Season.getSeasonByID(seasonID));
        source.sendFeedback((() -> Message.getFeedbackText("Successfully set season \"" + seasonID + "\"")), true);
        return 0;
    }
    public static int setWeather(String weatherID, ServerCommandSource source) {
        WeatherObject weather = Weather.getWeatherByID(weatherID);
        Weather.setWeather(weather);
        Weather.setIsNight(weather.isNightly());
        source.sendFeedback((() -> Message.getFeedbackText("Successfully set weather \"" + weatherID + "\"")), true);
        return 0;
    }
    public static int setLang(String lang, ServerCommandSource source) {
        Config.setLang(lang);
        source.sendFeedback((() -> Message.getFeedbackText("Successfully set lang \"" + lang + "\"")), true);
        return 0;
    }

    public static int reload(ServerCommandSource source) {
        SeasonsAPI.setLoaded(false);
        Config.reloadAll();
        Season.reloadFromConfig();
        Weather.reloadFromConfig();
        SeasonsAPI.setLoaded(true);
        source.sendFeedback(() -> Message.getFeedbackText("Seasons configs successfully reloaded!"), true);
        return 0;
    }

}
