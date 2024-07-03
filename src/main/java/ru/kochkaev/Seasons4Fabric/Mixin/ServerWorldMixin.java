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
import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.Service.Weather;
import ru.kochkaev.Seasons4Fabric.Util.Title;
//import ru.kochkaev.Seasons4Fabric.Util.Title;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
        extends World implements StructureWorldAccess, AttachmentTarget {

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    //    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
//        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
//    }
    //@Inject(method = "tick", at=@At(value="INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"))
    //@Inject(method = "tick", at=@At("HEAD"))
    @Inject(method = "tick", at=@At("HEAD"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if (this.properties.getTimeOfDay() >= Config.getLong("conf.tick.day.start") && Weather.isNight() && Config.getLong("conf.tick.day.end") >= this.properties.getTimeOfDay()) Weather.setChancedWeatherInCurrentSeason(this.toServerWorld());
        if (this.properties.getTimeOfDay() >= Config.getLong("conf.tick.day.end") && !Weather.isNight()) Weather.setWeather(Weather.getWeatherByID("NIGHT"), this.toServerWorld());
        //Main.getLogger().info(String.valueOf(this.properties.getTime()));
        Title.showActionBar(this.getServer().getPlayerManager());

    }

    //@Inject(method = "tick", at=@At("HEAD"))
    //public void tick(){
    //    Title.getInstance().showSubtitle();
    //}

}
