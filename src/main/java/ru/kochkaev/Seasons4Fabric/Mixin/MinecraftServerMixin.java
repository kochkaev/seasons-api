package ru.kochkaev.Seasons4Fabric.Mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.EffectsTicker;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract ServerWorld getOverworld();

    @Inject(method = "loadWorld", at=@At("TAIL"))
    protected void loadWorld(CallbackInfo ci){
        EffectsTicker.setWorld(this.getOverworld());
    }

}
