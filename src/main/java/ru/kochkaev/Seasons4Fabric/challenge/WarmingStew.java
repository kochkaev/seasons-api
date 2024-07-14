package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;
import java.util.List;

public class WarmingStew extends ChallengeObject {

    public WarmingStew() {
        super(Config.getLang().getString("lang.effect.warmingStew.message.trigger"), Collections.singletonList(Weather.COLD), true);
    }

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

    @Override
    public void challengeEnd(ServerPlayerEntity player) {

    }

    public void onConsume(List<Object> args) {
        if (isAllowed()){
            ServerPlayerEntity player = (ServerPlayerEntity) args.get(0);
            for (ItemStack stew : stews)
                if (player.getActiveItem() == stew) {
                    sendMessage(player, Config.getLang().getString("lang.effect.warmingStew.message.get"));
                    giveEffect(player, StatusEffects.REGENERATION, 20 * 10, 0);
                }
        }
    }
}
