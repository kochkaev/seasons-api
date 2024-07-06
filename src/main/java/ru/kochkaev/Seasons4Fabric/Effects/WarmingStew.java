package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class WarmingStew extends EffectObject {

    private ItemStack[] stews = { Items.BEETROOT_SOUP.getDefaultStack(), Items.MUSHROOM_STEW.getDefaultStack(), Items.RABBIT_STEW.getDefaultStack() };

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.warmingStew.message.trigger");
        this.isGood = true;
        this.weathers = Collections.singletonList(Weather.COLD);
        registerOnEventMethod("ON_CONSUME", this::onConsume);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls) {
        return 0;
    }

    public void onConsume(Object... args) {
        ServerPlayerEntity player = (ServerPlayerEntity) args[0];
        for (ItemStack stew : stews) if (player.getActiveItem() == stew) {
            sendMessage(player, Config.getLang().getString("lang.effect.warmingStew.message.get"));
            effect(player, StatusEffects.REGENERATION, 20*10, 0);
        }
    }
}
