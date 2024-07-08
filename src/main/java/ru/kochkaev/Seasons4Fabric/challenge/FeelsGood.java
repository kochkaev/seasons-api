package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class FeelsGood extends ChallengeObject {

    @Override
    public void register(){
        this.triggerMessage = Config.getLang().getString("lang.effect.feelsGood.message.trigger");
        this.weathers = Collections.singletonList(Weather.WARM);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction){
        boolean wearArmor = false;
        for (ItemStack item : player.getArmorItems()) wearArmor = item.getItem() != Items.AIR || (wearArmor);
        if (!wearArmor) {
            if (countOfInARowCalls == 0) {
                sendMessage(player, Config.getLang().getString("lang.effect.feelsGood.message.get"));
                giveEffect(player, StatusEffects.SPEED);
            }
            return countOfInARowCalls+1;
        }
        else if (wearArmor && countOfInARowCalls>0) {
            sendMessage(player, Config.getLang().getString("lang.effect.feelsGood.message.remove"));
            removeEffect(player, StatusEffects.SPEED);
        }
        return  0;
    }

}
