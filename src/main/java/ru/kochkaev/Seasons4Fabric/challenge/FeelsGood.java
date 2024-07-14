package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class FeelsGood extends ChallengeObject {

    public FeelsGood() {
        super(Config.getLang().getString("lang.effect.feelsGood.message.trigger"), Collections.singletonList(Weather.WARM), true);
    }

    @Override
    public void register(){

    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction){
        boolean wearArmor = false;
        for (ItemStack item : player.getArmorItems()) wearArmor = item.getItem() != Items.AIR || (wearArmor);
        if (!wearArmor) {
            if (countOfInARowCalls == 0) {
                sendMessage(player, Config.getLang().getString("lang.effect.feelsGood.message.get"));
                giveEffect(player, StatusEffects.SPEED);
                spawnParticles(player, ParticleTypes.HAPPY_VILLAGER, false, 1, 10);
            }
            return countOfInARowCalls+1;
        }
        else if (wearArmor && countOfInARowCalls>0) {
            spawnParticles(player, ParticleTypes.ANGRY_VILLAGER, false, 1, 2);
            sendMessage(player, Config.getLang().getString("lang.effect.feelsGood.message.remove"));
            removeEffect(player, StatusEffects.SPEED);
        }
        return  0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {
        removeEffect(player, StatusEffects.SPEED);
    }

}
