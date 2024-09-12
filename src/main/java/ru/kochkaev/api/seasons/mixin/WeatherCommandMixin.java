package ru.kochkaev.api.seasons.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import ru.kochkaev.api.seasons.command.SeasonsCommand;
import ru.kochkaev.api.seasons.command.WeatherSuggestionProvider;

@Mixin(WeatherCommand.class)
public class WeatherCommandMixin {

    /**
     * @author kochkaev
     * @reason disable vanilla weathers sets
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("weather")
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("weather", StringArgumentType.string()).suggests(new WeatherSuggestionProvider()).executes(context -> SeasonsCommand.setWeather(StringArgumentType.getString(context, "weather"), context.getSource()))))
        );
    }

}
