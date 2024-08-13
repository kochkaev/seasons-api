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

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntity {


    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnect(CallbackInfo ci) {
        ChallengesTicker.removePlayer((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ChallengesTicker.removePlayer((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "onSpawn", at = @At("HEAD"))
    public void onSpawn(CallbackInfo ci) {
        ChallengesTicker.addPlayer((ServerPlayerEntity) (Object) this);
    }


    public boolean isSpectator() {
        return false;
    }

    public boolean isCreative() {
        return false;
    }
}
