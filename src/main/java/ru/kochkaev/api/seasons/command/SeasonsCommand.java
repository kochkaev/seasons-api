package ru.kochkaev.api.seasons.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.service.Weather;

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
        PlayerManager players = source.getServer().getPlayerManager();
        Season.setSeason(Season.getSeasonByID(seasonID), players);
        source.sendFeedback(() -> Text.literal(("&7Successfully set season \"" + Season.getCurrent().getName() + "&7\"").replaceAll("&", "§")), true);
        return 0;
    }
    public static int setWeather(String weatherID, ServerCommandSource source) {
        ServerWorld world = source.getServer().getOverworld();
        Weather.setWeather(Weather.getWeatherByID(weatherID), world);
        source.sendFeedback(() -> Text.literal(("&7Successfully set weather \"" + Weather.getCurrent().getName() + "&7\"").replaceAll("&", "§")), true);
        return 0;
    }
    public static int setLang(String lang, ServerCommandSource source) {
        Config.setLang(lang);
        source.sendFeedback(() -> Text.literal(("&7Successfully set lang \"" + lang + "&7\"").replaceAll("&", "§")), true);
        return 0;
    }

    public static int reload(ServerCommandSource source) {
        Config.getModConfig("API").reload();
        Weather.reloadFromConfig(source.getServer().getOverworld());
        Season.reloadFromConfig(source.getServer().getPlayerManager());
        source.sendFeedback(() -> Text.literal(("&7Seasons4Fabric configs has been successfully reloaded!").replaceAll("&", "§")), true);
        return 0;
    }

}
