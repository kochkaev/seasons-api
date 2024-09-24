package ru.kochkaev.api.seasons.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.command.SeasonsCommand;
import ru.kochkaev.api.seasons.command.WeatherSuggestionProvider;
import ru.kochkaev.api.seasons.provider.Config;

@Mixin(WeatherCommand.class)
public class WeatherCommandMixin {

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void registerSeasons(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        if (SeasonsAPI.isLoaded()){
            dispatcher.register(CommandManager.literal("weather")
                    .then(CommandManager.literal("set")
                            .then(CommandManager.argument("weather", StringArgumentType.string()).suggests(new WeatherSuggestionProvider()).executes(context -> SeasonsCommand.setWeather(StringArgumentType.getString(context, "weather"), context.getSource()))))
            );
            ci.cancel();
        }
    }

}
