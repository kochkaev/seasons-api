package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Objects.EventObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class Devastation extends EffectObject {

    private EventObject onHeal;
    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.devastation.message.trigger");
        this.weathers = Collections.singletonList(Weather.STORMY);
        onHeal = registerOnEventMethod("ON_HEAL", this::onHeal);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        return 0;
    }

    public void onHeal(Object... args) {
        LivingEntity entity = (LivingEntity) args[0];
        if (!entity.hasStatusEffect(StatusEffects.REGENERATION)) {
            onHeal.cancelEvent();
        }
    }
}
