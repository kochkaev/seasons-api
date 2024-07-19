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

public class SeasonCommand {

    public SeasonCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("season")
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("season", StringArgumentType.string()).suggests(new SeasonSuggestionProvider()).executes(context -> SeasonsCommand.setSeason(StringArgumentType.getString(context, "season"), context.getSource()))))
        );
    }

}
