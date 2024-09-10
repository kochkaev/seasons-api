package ru.kochkaev.api.seasons.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import ru.kochkaev.api.seasons.ChallengesTicker;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.provider.Config;
import ru.kochkaev.api.seasons.object.ChallengeObject;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.provider.Challenge;
import ru.kochkaev.api.seasons.provider.Season;
import ru.kochkaev.api.seasons.provider.Weather;
import ru.kochkaev.api.seasons.util.Message;

public class SeasonsCommand {

    public SeasonsCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("seasons")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.literal("season")
                                .then(CommandManager.argument("season", StringArgumentType.string()).suggests(new SeasonSuggestionProvider())
                                        .executes(context -> setSeason(StringArgumentType.getString(context, "season"), context.getSource()))
                                )
                        )
                        .then(CommandManager.literal("weather")
                                .then(CommandManager.argument("weather", StringArgumentType.string()).suggests(new WeatherSuggestionProvider())
                                        .executes(context -> setWeather(StringArgumentType.getString(context, "weather"), context.getSource()))
                                )
                        )
                        .then(CommandManager.literal("lang")
                                .then(CommandManager.argument("lang", StringArgumentType.string()).suggests(new LangSuggestionProvider())
                                        .executes(context -> setLang(StringArgumentType.getString(context, "lang"), context.getSource()))
                                )
                        )
                        .then(CommandManager.literal("challenge")
                                .then(CommandManager.literal("forceAllow")
                                        .then(CommandManager.argument("challenge", StringArgumentType.string()).suggests(new ChallengeSuggestionProvider())
                                                .executes(context -> forceAllowChallenge(StringArgumentType.getString(context, "challenge"), context.getSource()))
                                        )
                                )
                                .then(CommandManager.literal("forceDisable")
                                        .then(CommandManager.argument("challenge", StringArgumentType.string()).suggests(new ChallengeSuggestionProvider())
                                                .executes(context -> forceDisableChallenge(StringArgumentType.getString(context, "challenge"), context.getSource()))
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("get")
                        .then(CommandManager.literal("season").executes(context -> getSeason(context.getSource())))
                        .then(CommandManager.literal("weather").executes(context -> getWeather(context.getSource())))
                        .then(CommandManager.literal("lang").executes(context -> getLang(context.getSource())))
                        .then(CommandManager.literal("challenges").executes(context -> getChallenges(context.getSource())))
                )
                .then(CommandManager.literal("reload").executes(context -> reload(context.getSource())))
        );
    }

    public static int setSeason(String seasonID, ServerCommandSource source) {
        Season.setSeasonIgnoringPrevious(Season.getSeasonAnyBranchByID(seasonID));
        source.sendFeedback((() -> Message.getFeedbackText("Successfully set season \"" + seasonID + "\"")), true);
        return 0;
    }
    public static int getSeason(ServerCommandSource source) {
        source.sendFeedback((() -> Message.getFeedbackText("Current season is \"" + Season.getCurrent().getName() + "\"")), false);
        return 0;
    }
    public static int setWeather(String weatherID, ServerCommandSource source) {
        WeatherObject weather = Weather.getWeatherByID(weatherID);
        Weather.setWeather(weather);
        Weather.setIsNight(weather.isNightly());
        source.sendFeedback((() -> Message.getFeedbackText("Successfully set weather \"" + weatherID + "\"")), true);
        return 0;
    }
    public static int getWeather(ServerCommandSource source) {
        source.sendFeedback((() -> Message.getFeedbackText("Current weather is \"" + Weather.getCurrent().getName() + "\"")), false);
        return 0;
    }
    public static int setLang(String lang, ServerCommandSource source) {
        Config.setLang(lang);
        source.sendFeedback((() -> Message.getFeedbackText("Successfully set lang \"" + lang + "\"")), true);
        return 0;
    }
    public static int getLang(ServerCommandSource source) {
        source.sendFeedback((() -> Message.getFeedbackText("Current lang is \"" + Config.getCurrentLang() + "\"")), false);
        return 0;
    }
    public static int forceAllowChallenge(String challenge, ServerCommandSource source) {
        ChallengesTicker.forceAllowChallenge(Challenge.getChallengeByID(challenge));
        source.sendFeedback((() -> Message.getFeedbackText("Successfully force allowed challenge \"" + challenge + "\"")), true);
        return 0;
    }
    public static int forceDisableChallenge(String challenge, ServerCommandSource source) {
        ChallengesTicker.forceDisableChallenge(Challenge.getChallengeByID(challenge));
        source.sendFeedback((() -> Message.getFeedbackText("Successfully force disabled challenge \"" + challenge + "\"")), true);
        return 0;
    }
    public static int getChallenges(ServerCommandSource source) {
        source.sendFeedback((() -> Message.getFeedbackText("Currently allowed challenges are " + ChallengesTicker.getAllowedChallenges().stream().map(ChallengeObject::getID).toList())), false);
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

//    private <S, T extends ArgumentBuilder<S, T>> ArgumentBuilder<S, T> getSeasonSetBranch(){
//
//    }

}
