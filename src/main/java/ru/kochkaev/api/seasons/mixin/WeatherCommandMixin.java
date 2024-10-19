package ru.kochkaev.api.seasons.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
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

    /**
     * @author
     * @reason
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
//    private WeatherCommand def = (WeatherCommand) (Object) this;

//    @Inject(method = "register", at = @At("HEAD"))
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder)CommandManager.literal("weather").requires((source) -> {
                        return source.hasPermissionLevel(2);
                    }).then(
//                            (!SeasonsAPI.isLoaded()) ?
//                            (((LiteralArgumentBuilder)CommandManager.literal("clear").executes((context) -> {
//                                return WeatherCommandInvoker.invokeExecuteClear((ServerCommandSource)context.getSource(), -1);
//                            })).then(CommandManager.argument("duration", TimeArgumentType.time(1)).executes((context) -> {
//                                return WeatherCommandInvoker.invokeExecuteClear((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "duration"));
//                            }))).then(((LiteralArgumentBuilder)CommandManager.literal("rain").executes((context) -> {
//                                return WeatherCommandInvoker.invokeExecuteRain((ServerCommandSource)context.getSource(), -1);
//                            })).then(CommandManager.argument("duration", TimeArgumentType.time(1)).executes((context) -> {
//                                return WeatherCommandInvoker.invokeExecuteRain((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "duration"));
//                            }))).then(((LiteralArgumentBuilder)CommandManager.literal("thunder").executes((context) -> {
//                                return WeatherCommandInvoker.invokeExecuteThunder((ServerCommandSource)context.getSource(), -1);
//                            })).then(CommandManager.argument("duration", TimeArgumentType.time(1)).executes((context) -> {
//                                return WeatherCommandInvoker.invokeExecuteThunder((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "duration"));
//                    })))
//                        :
                        (CommandManager.literal("set")
                                .then(CommandManager.argument("weather", StringArgumentType.string()).suggests(new WeatherSuggestionProvider())
                                        .executes(context -> SeasonsCommand.setWeather(StringArgumentType.getString(context, "weather"), context.getSource())))))
        );
    }
}

