package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WarmingStew extends ChallengeObject {

    public WarmingStew() {
        super(Config.getLang().getString("lang.effect.warmingStew.message.trigger"), Collections.singletonList(Weather.COLD), true);
    }

    private final List<Item> stews = Arrays.asList(Items.BEETROOT_SOUP, Items.MUSHROOM_STEW, Items.RABBIT_STEW);

    @Override
    public void register() {
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
            ServerPlayerEntity player = (ServerPlayerEntity) args.getFirst();
            if (stews.contains(player.getActiveItem().getItem())) {
                sendMessage(player, Config.getLang().getString("lang.effect.warmingStew.message.get"));
                spawnParticles(player, ParticleTypes.HAPPY_VILLAGER, false, 1, 10);
                giveEffect(player, StatusEffects.REGENERATION, 20 * 10, 0);
            }
        }
    }
}
