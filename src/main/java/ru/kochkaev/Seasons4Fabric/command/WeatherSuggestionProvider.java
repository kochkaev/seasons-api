package ru.kochkaev.Seasons4Fabric.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.concurrent.CompletableFuture;

public class WeatherSuggestionProvider implements SuggestionProvider<ServerCommandSource> {


    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        for (Weather weather : Weather.getAll()) {
            builder.suggest(weather.name());
        }
        return builder.buildFuture();
    }
}
