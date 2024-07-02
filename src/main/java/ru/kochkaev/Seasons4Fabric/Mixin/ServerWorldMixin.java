package ru.kochkaev.Seasons4Fabric.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;
import ru.kochkaev.Seasons4Fabric.Util.Title;

@Mixin(net.minecraft.server.world.ServerWorld.class)
public abstract class ServerWorldMixin {

//    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
//        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
//    }
    @Inject(method = "tick", at=@At(value="INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"))
    public void tickMorningInjected(){
        Weather.setChancedWeatherInCurrentSeason();
    }

    @Inject(method = "tick", at=@At("HEAD"))
    public void tickInjected(){
        Title.getInstance().showSubtitle();
    }

}
