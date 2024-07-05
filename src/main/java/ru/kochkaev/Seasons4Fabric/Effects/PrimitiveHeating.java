package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStackSet;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class PrimitiveHeating extends EffectObject {

    private static ItemStack[] hots = {Items.LAVA_BUCKET.getDefaultStack(), Items.BLAZE_POWDER.getDefaultStack(), Items.BLAZE_ROD.getDefaultStack(),
            Items.DRAGON_BREATH.getDefaultStack(), Items.MAGMA_CREAM.getDefaultStack()};

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.primitiveHeating.message.trigger");
        this.isGood = true;
        this.weathers = Collections.singletonList(Weather.COLD);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls) {
        boolean  isHot  = false;
        for (ItemStack item : hots) if (player.getInventory().contains(item)) isHot = true;
        if (isHot && countOfInARowCalls==0) {
            sendMessage(player, Config.getLang().getString("lang.effect.primitiveHeating.message.get"));
            effect(player, StatusEffects.RESISTANCE, 0);
            return countOfInARowCalls+1;
        }
        else if (!isHot && countOfInARowCalls>0) {
            sendMessage(player, Config.getLang().getString("lang.effect.primitiveHeating.message.remove"));
            removeEffect(player, StatusEffects.RESISTANCE);
        }
        return 0;
    }
}
