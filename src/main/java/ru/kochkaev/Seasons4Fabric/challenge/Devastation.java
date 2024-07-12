package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.IFunc;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.object.EventObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Devastation extends ChallengeObject {

    private static EventObject onHeal;
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

    public void onHeal(List<Object> args) {
        LivingEntity entity = (LivingEntity) args.get(0);
        if (!entity.hasStatusEffect(StatusEffects.REGENERATION)) {
            onHeal.cancelEvent();
        }
    }
}
