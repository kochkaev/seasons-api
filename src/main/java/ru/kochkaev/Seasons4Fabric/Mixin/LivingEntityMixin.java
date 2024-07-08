package ru.kochkaev.Seasons4Fabric.Mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kochkaev.Seasons4Fabric.Objects.EventObject;
import ru.kochkaev.Seasons4Fabric.Service.Effect;
import ru.kochkaev.Seasons4Fabric.Service.Event;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends Entity {

    @Unique
    private static final EventObject onHealEvent = Event.getEventByID("ON_HEAL");

    @Inject(method = "heal", at=@At("HEAD"))
    public void heal(float amount, CallbackInfo ci)  {
        if (this.getType() == EntityType.PLAYER) {
            onHealEvent.onEvent((LivingEntity) (Object) this);
            if (onHealEvent.isCancelledAndReset()) return;
        }
    }


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
