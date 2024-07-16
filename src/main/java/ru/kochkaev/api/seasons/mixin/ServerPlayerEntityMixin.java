package ru.kochkaev.api.seasons.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.api.seasons.ChallengesTicker;
import ru.kochkaev.api.seasons.object.EventObject;
import ru.kochkaev.api.seasons.service.Event;

import java.util.Collections;

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
            onConsumeEvent.onEvent(Collections.singletonList((ServerPlayerEntity) (Object) this));
            if (onConsumeEvent.isCancelledAndReset()) return;
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ChallengesTicker.removePlayer((ServerPlayerEntity) (Object) this);
//        Main.getLogger().info("Player " + this.getName().getString() + " died");
    }

    @Inject(method = "onSpawn", at = @At("HEAD"))
    public void onSpawn(CallbackInfo ci) {
        ChallengesTicker.addPlayer((ServerPlayerEntity) (Object) this);
//        Main.getLogger().info("Player " + this.getName().getString() + " spawned");
    }

//    @Inject(method = "getRespawnTarget", at = @At("RETURN"))
//    public TeleportTarget getRespawnTarget(boolean alive, TeleportTarget.PostDimensionTransition postDimensionTransition, CallbackInfo ci) {
//        ChallengesTicker.addPlayer((ServerPlayerEntity) (Object) this);
//        return null;
//    }

//    @Inject(method = "teleportTo" , at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerCommonNetworkHandler;sendPacket()V"))
//    private void onRespawn(CallbackInfo ci) {
//        ChallengesTicker.addPlayer((ServerPlayerEntity) (Object) this);
//    }

    public boolean isSpectator() {
        return false;
    }

    public boolean isCreative() {
        return false;
    }
}
