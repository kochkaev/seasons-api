package ru.kochkaev.Seasons4Fabric.mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.function.Supplier;

@Mixin(net.minecraft.server.world.ServerWorld.class)
public abstract class ServerWorldMixin {

//    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
//        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
//    }
    @Inject(method = "tick", at=@At(value="INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"))
    public void tickInjected(){
        Weather.setChancedWeatherInCurrentSeason();
    }

    @Overwrite
    private void tickWeather(){

    }

}
