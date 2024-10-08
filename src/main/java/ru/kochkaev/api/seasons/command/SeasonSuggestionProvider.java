package ru.kochkaev.api.seasons.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.provider.Season;

import java.util.concurrent.CompletableFuture;

public class SeasonSuggestionProvider implements SuggestionProvider<ServerCommandSource> {


    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        for (SeasonObject season : Season.getAll()) {
            builder.suggest(season.getId());
        }
        return builder.buildFuture();
    }
}
