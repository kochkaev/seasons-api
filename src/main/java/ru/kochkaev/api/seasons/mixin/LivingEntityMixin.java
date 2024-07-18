package ru.kochkaev.api.seasons.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.api.seasons.object.EventObject;
import ru.kochkaev.api.seasons.service.Event;

import java.util.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    public void heal(float amount, CallbackInfo ci)  {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getType() == EntityType.PLAYER) {
            EventObject onHealEvent = Event.getEventByID("ON_HEAL");
            onHealEvent.onEvent(Collections.singletonList(entity));
            if (onHealEvent.isCancelledAndReset()) {
                ci.cancel();
            }
        }
    }
}
