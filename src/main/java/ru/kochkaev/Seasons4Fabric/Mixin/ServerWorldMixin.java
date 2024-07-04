package ru.kochkaev.Seasons4Fabric.Mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.Service.Weather;
import ru.kochkaev.Seasons4Fabric.Util.Title;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
        extends World implements StructureWorldAccess, AttachmentTarget {

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, ServerWorldProperties worldProperties) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
        this.worldProperties = worldProperties;
    }

    @Shadow
    private final ServerWorldProperties worldProperties;

    @Inject(method = "tick", at=@At("HEAD"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if ((this.properties.getTimeOfDay()%24000L >= Config.getLong("conf.tick.day.start")) && (Weather.isNight()) && (Config.getLong("conf.tick.day.end") > this.properties.getTimeOfDay()%24000L)) Weather.setDay(this.toServerWorld());
        if ((this.properties.getTimeOfDay()%24000L >= Config.getLong("conf.tick.day.end")) && (!Weather.isNight())) Weather.setNight(this.toServerWorld());
        Title.showActionBar(this.getServer().getPlayerManager());
        if ((this.worldProperties.isRaining()!=Weather.getCurrent().getRaining()) || (this.worldProperties.isThundering()!=Weather.getCurrent().getThundering())) this.setWeather(-1, -1, false, false);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void resetWeather() {
        return;
    }

    /**
     * @author
     * @reason
     */

    @Overwrite
    public void setWeather(int clearDuration, int rainDuration, boolean raining, boolean thundering) {
        Weather weather = Weather.getCurrent();
        Main.getLogger().info(String.valueOf(weather));
        this.worldProperties.setClearWeatherTime(-1);
        this.worldProperties.setRainTime(-1);
        this.worldProperties.setThunderTime(-1);
        this.worldProperties.setRaining(weather.getRaining());
        this.worldProperties.setThundering(weather.getThundering());
        return;
    }

}
