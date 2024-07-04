package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Overwrite;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class FeelsGood extends EffectObject {

    public void register(){
        this.triggerMessage = Config.getLang().getString("lang.effect.feelsGood.message.trigger");
        this.isGood = true;
        this.weathers = Collections.singletonList(Weather.WARM);
    }

    public void logic(ServerPlayerEntity player, int countOfInARowCalls){
        boolean wearArmor = false;
        for (ItemStack item : player.getInventory().player.getArmorItems()) wearArmor = (item != null) || (wearArmor);
        if (!wearArmor && countOfInARowCalls == 0) {
            sendMessage(player, Config.getLang().getString("lang.effect.feelsGood.message.get"));
            effect(player, StatusEffects.SPEED);
            countOfInARowCalls++;
        }
        else if (wearArmor && countOfInARowCalls>0) {
            sendMessage(player, Config.getLang().getString("lang.effect.feelsGood.message.remove"));
            countOfInARowCalls = 0;
        }
    }

}
