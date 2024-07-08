package ru.kochkaev.Seasons4Fabric.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.ChallengesTicker;
import ru.kochkaev.Seasons4Fabric.object.EventObject;
import ru.kochkaev.Seasons4Fabric.service.Event;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntity {

    //@Unique
    //private final EventObject onConsumeEvent = Event.getEventByID("ON_CONSUME");

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnect(CallbackInfo ci) {
        ChallengesTicker.removePlayer((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "consumeItem", at = @At("HEAD"))
    protected void consumeItem(CallbackInfo ci){
        if (!this.activeItemStack.isEmpty() && this.isUsingItem()) {
            EventObject onConsumeEvent = Event.getEventByID("ON_CONSUME");
            onConsumeEvent.onEvent((ServerPlayerEntity) (Object) this);
            if (onConsumeEvent.isCancelledAndReset()) return;
        }
    }

    public boolean isSpectator() {
        return false;
    }

    public boolean isCreative() {
        return false;
    }
}
