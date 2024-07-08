package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class FluffyCoat extends EffectObject {

    @Override
    public void register(){
        this.triggerMessage = Config.getLang().getString("lang.effect.fluffyCoat.message.trigger");
        this.weathers = Collections.singletonList(Weather.SNOWY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction){
        boolean wearArmor = false;
        for (ItemStack item : player.getInventory().player.getArmorItems()) wearArmor = (item != null) || (wearArmor);
        if (!wearArmor && countOfInARowCalls == 0) {
            sendMessage(player, Config.getLang().getString("lang.effect.fluffyCoat.message.get"));
            giveEffect(player, StatusEffects.RESISTANCE, 0);
            return countOfInARowCalls+1;
        }
        else if (wearArmor && countOfInARowCalls>0) {
            sendMessage(player, Config.getLang().getString("lang.effect.fluffyCoat.message.remove"));
            removeEffect(player, StatusEffects.RESISTANCE);
        }
        return 0;
    }

}
