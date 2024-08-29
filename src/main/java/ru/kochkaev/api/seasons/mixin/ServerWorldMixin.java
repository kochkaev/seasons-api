package ru.kochkaev.api.seasons.mixin;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.service.Config;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Task;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.Title;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
        extends World implements StructureWorldAccess {

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, ServerWorldProperties worldProperties) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
        this.worldProperties = worldProperties;
    }


//    @Final
//    @Shadow(prefix="seasonsAPI$")
//    private abstract final ServerWorldProperties seasonsAPI$worldProperties;
    @Shadow
    private final ServerWorldProperties worldProperties;


    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if (SeasonsAPI.isLoaded()){
            if ((this.properties.getTimeOfDay() % 24000L >= Config.getModConfig("API").getConfig().getLong("conf.tick.day.start")) && (Boolean.TRUE.equals(Weather.isNight())) && (Config.getModConfig("API").getConfig().getLong("conf.tick.day.end") > this.properties.getTimeOfDay() % 24000L))
                Weather.setDay();
            if ((this.properties.getTimeOfDay() % 24000L >= Config.getModConfig("API").getConfig().getLong("conf.tick.day.end")) && (Boolean.FALSE.equals(Weather.isNight())))
                Weather.setNight();
            if (Config.getModConfig("API").getConfig().getBoolean("conf.enable.title.actionbar")) Title.showActionBar();
            if ((this.worldProperties.isRaining() != Weather.getCurrent().getRaining()) || (this.worldProperties.isThundering() != Weather.getCurrent().getThundering()))
                this.setWeather(-1, -1, false, false);
            Task.runTasks();
        }
    }


    /**
     * @author kochkaev
     * @reason Disable weather reset.
     */
    @VisibleForTesting
    @Overwrite
    public void resetWeather() {
    }

    /**
     * @author kochkaev
     * @reason Disable weather reset.
     */

    @Overwrite
    public void setWeather(int clearDuration, int rainDuration, boolean raining, boolean thundering) {
        WeatherObject weather = Weather.getCurrent();
        this.worldProperties.setClearWeatherTime(-1);
        this.worldProperties.setRainTime(-1);
        this.worldProperties.setThunderTime(-1);
        this.worldProperties.setRaining(weather.getRaining());
        this.worldProperties.setThundering(weather.getThundering());
    }

}
