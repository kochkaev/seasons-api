package ru.kochkaev.api.seasons.mixin;

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
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.object.WeatherObject;
import ru.kochkaev.api.seasons.service.Task;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.Title;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
        extends World implements StructureWorldAccess, AttachmentTarget {

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, ServerWorldProperties worldProperties) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
        this.worldProperties = worldProperties;
    }

    //private static final EventObject onBlockChangeEvent = Event.getEventByID("ON_BLOCK_CHANGE");

    @Shadow
    private final ServerWorldProperties worldProperties;

//    @Unique
//    private static final EventObject onBlockChangeEvent = Event.getEventByID("ON_BLOCK_CHANGE");

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if ((this.properties.getTimeOfDay()%24000L >= Config.getConfig().getLong("conf.tick.day.start")) && (Weather.isNight()) && (Config.getConfig().getLong("conf.tick.day.end") > this.properties.getTimeOfDay()%24000L)) Weather.setDay(this.toServerWorld());
        if ((this.properties.getTimeOfDay()%24000L >= Config.getConfig().getLong("conf.tick.day.end")) && (!Weather.isNight())) Weather.setNight(this.toServerWorld());
        Title.showActionBar(Objects.requireNonNull(this.getServer()).getPlayerManager());
        if ((this.worldProperties.isRaining()!=Weather.getCurrent().getRaining()) || (this.worldProperties.isThundering()!=Weather.getCurrent().getThundering())) this.setWeather(-1, -1, false, false);
        Task.runTasks();
    }

//    @Inject(method = "onBlockChanged", at = @At("HEAD"))
//    public void onBlockChanged(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci){
//        EventObject onBlockChangeEvent = Event.getEventByID("ON_BLOCK_CHANGE");
//        onBlockChangeEvent.onEvent(Arrays.asList(pos, oldBlock, newBlock));
//        if (onBlockChangeEvent.isCancelledAndReset()) return;
//        if (onBlockChangeEvent.isReturnedAndReset()) newBlock = (BlockState) onBlockChangeEvent.getReturned();
//    }

//    @Inject(at = @At("HEAD"), method = "onBlockChanged", cancellable = true)
//    public void onBlockChanged(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
//        onBlockChangeEvent.onEvent(Arrays.asList(oldBlock, newBlock));
//        if (onBlockChangeEvent.isCancelledAndReset()) {
//            return;
//        }
//    }
//    @ModifyVariable(at = @At("HEAD"), method = "onBlockChanged", ordinal = 1)
//    public BlockState onBlockChangedEvent(BlockState newBlock) {
//        if (onBlockChangeEvent.isReturnedAndReset()) return (BlockState) onBlockChangeEvent.getReturned();
//        return newBlock;
//    }

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
        WeatherObject weather = Weather.getCurrent();
        WeatherObject prevWeather = Weather.getPreviousCurrent();
        //Main.getLogger().info(String.valueOf(weather));
        this.worldProperties.setClearWeatherTime(-1);
        this.worldProperties.setRainTime(-1);
        this.worldProperties.setThunderTime(-1);
        this.worldProperties.setRaining(weather.getRaining());
        this.worldProperties.setThundering(weather.getThundering());
        return;
    }

}
