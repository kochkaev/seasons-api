package ru.kochkaev.Seasons4Fabric.Mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.EffectsTicker;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Objects.EventObject;
import ru.kochkaev.Seasons4Fabric.Service.Effect;
import ru.kochkaev.Seasons4Fabric.Service.Event;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntity {

    @Unique
    private final EventObject onConsumeEvent = Event.getEventByID("ON_CONSUME");

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnect(CallbackInfo ci) {
        EffectsTicker.removePlayer((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "consumeItem", at = @At("TAIL"))
    protected void consumeItem(CallbackInfo ci){
        if (!this.activeItemStack.isEmpty() && this.isUsingItem()) {
           onConsumeEvent.onEvent((ServerPlayerEntity) (Object) this);
        }
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
