package ru.kochkaev.Seasons4Fabric.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.Service.Season;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

public class Seasons4FabricCommand {

    public Seasons4FabricCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("seasons4fabric")
                .then(CommandManager.literal("set")
                        .then(CommandManager.literal("season")
                                .then(CommandManager.argument("season", StringArgumentType.string()).suggests(new SeasonSuggestionProvider()).executes(context -> setSeason(StringArgumentType.getString(context, "season"), (ServerCommandSource)context.getSource()))))
                        .then(CommandManager.literal("weather")
                                .then(CommandManager.argument("weather", StringArgumentType.string()).suggests(new WeatherSuggestionProvider()).executes(context -> setWeather(StringArgumentType.getString(context, "weather"), (ServerCommandSource)context.getSource())))))
                .then(CommandManager.literal("reload").executes(context -> reload((ServerCommandSource)context.getSource())))
        );
    }

    private static int setSeason(String seasonID, ServerCommandSource source) {
        PlayerManager players = source.getServer().getPlayerManager();
        Season.setSeason(Season.getSeasonByID(seasonID), players);
        source.sendFeedback(() -> {
            return Text.literal(("&7Successfully set season \"" + Season.getCurrent().getName() + "&7\"").replaceAll("&", "§"));
        }, true);
        return 0;
    }
    public static int setWeather(String weatherID, ServerCommandSource source) {
        ServerWorld world = source.getServer().getOverworld();
        Weather.setWeather(Weather.getWeatherByID(weatherID), world);
        source.sendFeedback(() -> {
            return Text.literal(("&7Successfully set weather \"" + Weather.getCurrent().getName() + "&7\"").replaceAll("&", "§"));
        }, true);
        return 0;
    }

    public static int reload(ServerCommandSource source) {
        Config.reload();
        Weather.reloadFromConfig(source.getServer().getOverworld());
        Season.reloadFromConfig(source.getServer().getPlayerManager());
        source.sendFeedback(() -> {
            return Text.literal(("&7Seasons4Fabric configs has been successfully reloaded!").replaceAll("&", "§"));
        }, true);
        return 0;
    }

    public static void help() {

    }

}
