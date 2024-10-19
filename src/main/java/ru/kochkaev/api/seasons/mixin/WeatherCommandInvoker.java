package ru.kochkaev.api.seasons.mixin;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WeatherCommand.class)
public interface WeatherCommandInvoker {

    @Invoker("executeClear")
    public static int invokeExecuteClear(ServerCommandSource source, int duration) {
        throw new AssertionError();
    }

    @Invoker("executeRain")
    public static int invokeExecuteRain(ServerCommandSource source, int duration) {
        throw new AssertionError();
    }

    @Invoker("executeThunder")
    public static int invokeExecuteThunder(ServerCommandSource source, int duration) {
        throw new AssertionError();
    }
}
