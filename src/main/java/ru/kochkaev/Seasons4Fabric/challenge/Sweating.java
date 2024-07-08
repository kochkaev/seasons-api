package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class Sweating extends ChallengeObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.sweating.message.trigger");
        this.weathers = Collections.singletonList(Weather.HOT);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        boolean isFullArmor = true;
        for (ItemStack armor : player.getArmorItems())
            if (armor.isEmpty()) {
                isFullArmor = false;
                break;
            }
        if (isFullArmor) {
            if (countOfInARowCalls == 0) sendMessage(player, Config.getLang().getString("lang.effect.sweating.message.get"));
            else if (countOfInARowCalls == ticksPerAction) giveEffect(player, StatusEffects.MINING_FATIGUE);
            else if (countOfInARowCalls%ticksPerAction == 0) damage(player);
            return countOfInARowCalls + 1;
        }
        else if (countOfInARowCalls > 0 && !isFullArmor) {
            sendMessage(player, Config.getLang().getString("lang.effect.sweating.message.remove"));
            return -1 - ticksPerAction;
        }
        else if (countOfInARowCalls == -1 && !isFullArmor) {
            removeEffect(player, StatusEffects.MINING_FATIGUE);
        }
        return 0;
    }
}
