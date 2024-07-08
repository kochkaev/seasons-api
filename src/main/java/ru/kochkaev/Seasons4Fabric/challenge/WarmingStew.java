package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class WarmingStew extends ChallengeObject {

    private ItemStack[] stews = { Items.BEETROOT_SOUP.getDefaultStack(), Items.MUSHROOM_STEW.getDefaultStack(), Items.RABBIT_STEW.getDefaultStack() };

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.warmingStew.message.trigger");
        this.weathers = Collections.singletonList(Weather.COLD);
        registerOnEventMethod("ON_CONSUME", this::onConsume);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        return 0;
    }

    public void onConsume(Object... args) {
        ServerPlayerEntity player = (ServerPlayerEntity) ((Object[]) args[0])[0];
        for (ItemStack stew : stews) if (player.getActiveItem() == stew) {
            sendMessage(player, Config.getLang().getString("lang.effect.warmingStew.message.get"));
            giveEffect(player, StatusEffects.REGENERATION, 20*10, 0);
        }
    }
}
