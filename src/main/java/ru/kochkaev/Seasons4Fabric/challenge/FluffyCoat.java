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

public class FluffyCoat extends ChallengeObject {

    public FluffyCoat() {
        super(Config.getLang().getString("lang.effect.fluffyCoat.message.trigger"), Collections.singletonList(Weather.SNOWY), true);
    }

    @Override
    public void register(){
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction){
        boolean doNotWearArmor = false;
        for (ItemStack item : player.getArmorItems()) doNotWearArmor = item.getItem() == Items.AIR || (doNotWearArmor);
        if (!doNotWearArmor) {
            if (countOfInARowCalls == 0) {
                sendMessage(player, Config.getLang().getString("lang.effect.fluffyCoat.message.get"));
                giveEffect(player, StatusEffects.RESISTANCE);
                spawnParticles(player, ParticleTypes.HAPPY_VILLAGER, false, 1, 10);
            }
            return countOfInARowCalls+1;
        }
        else if (doNotWearArmor && countOfInARowCalls>0) {
            sendMessage(player, Config.getLang().getString("lang.effect.fluffyCoat.message.remove"));
            spawnParticles(player, ParticleTypes.ANGRY_VILLAGER, false, 1, 2);
            removeEffect(player, StatusEffects.RESISTANCE);
        }
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {
        removeEffect(player, StatusEffects.RESISTANCE);
    }

}
