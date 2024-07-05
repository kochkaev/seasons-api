package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class Devastation extends EffectObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.devastation.message.trigger");
        this.isGood = false;
        this.weathers = Collections.singletonList(Weather.STORMY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls) {
        if (!player.hasStatusEffect(StatusEffects.REGENERATION)) {
        }
        return 0;
    }
}
