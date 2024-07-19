package ru.kochkaev.api.seasons.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import ru.kochkaev.api.seasons.config.Config;

import java.util.concurrent.CompletableFuture;

public class LangSuggestionProvider implements SuggestionProvider<ServerCommandSource> {


    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        for (String lang : Config.getLangs()) {
            builder.suggest(lang);
        }
        return builder.buildFuture();
    }
}