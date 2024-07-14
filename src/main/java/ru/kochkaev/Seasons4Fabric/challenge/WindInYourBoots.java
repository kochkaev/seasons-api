package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;
import java.util.Random;

public class WindInYourBoots extends ChallengeObject {

    public WindInYourBoots() {
        super(Config.getLang().getString("lang.effect.windInYourBoots.message.trigger"), Collections.singletonList(Weather.BREEZY), false);
    }

    @Override
    public void register() {
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (countOfInARowCalls >= 60 * 2) {
            int random = new Random().nextInt(10);
            if (random < 5 && !player.hasStatusEffect(StatusEffects.SPEED)) giveEffect(player, StatusEffects.SPEED, 20*10, 0);
            return 0;
        }
        else {
            return countOfInARowCalls+1;
        }
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {

    }
}
