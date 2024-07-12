package ru.kochkaev.Seasons4Fabric.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.IFunc;
import ru.kochkaev.Seasons4Fabric.IFuncRet;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.object.EventObject;
import ru.kochkaev.Seasons4Fabric.service.Event;
import ru.kochkaev.Seasons4Fabric.service.Task;

import java.util.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    //@Unique
    //private static final EventObject onHealEvent = Event.getEventByID("ON_HEAL");

    /**
     * @author Kochkaev
     * @reason
     */
    @Overwrite
    public void heal(float amount)  {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getType() == EntityType.PLAYER) {
            EventObject onHealEvent = Event.getEventByID("ON_HEAL");
            onHealEvent.onEvent(Collections.singletonList(entity));
            if (onHealEvent.isCancelledAndReset()) {
                return;
            }
        }
        float f = entity.getHealth();
        if (f > 0.0F) {
            entity.setHealth(f + amount);
        }
    }

//    @Inject(method = "tick", at = @At("HEAD"))
//    public void tick(CallbackInfo ci) {
//
//    }
}
