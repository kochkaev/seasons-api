package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;
import java.util.Random;

public class WindInYourBoots extends EffectObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.windInYourBoots.message.trigger");
        this.isGood = true;
        this.weathers = Collections.singletonList(Weather.BREEZY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls) {
        if (countOfInARowCalls >= 60 * 2) {
            int random = new Random().nextInt(10);
            if (random < 5 && !player.hasStatusEffect(StatusEffects.SPEED)) effect(player, StatusEffects.SPEED, 20*10, 0);
            return 0;
        }
        else {
            return countOfInARowCalls+1;
        }
    }
}
