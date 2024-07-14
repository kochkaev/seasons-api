package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class PrimitiveHeating extends ChallengeObject {

    public PrimitiveHeating() {
        super(Config.getLang().getString("lang.effect.primitiveHeating.message.trigger"), Collections.singletonList(Weather.COLD), true);
    }

    private static Item[] hots = {Items.LAVA_BUCKET, Items.BLAZE_POWDER, Items.BLAZE_ROD,
            Items.DRAGON_BREATH, Items.MAGMA_CREAM};

    @Override
    public void register() {
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        boolean  isHot  = false;
        for (Item item : hots) if (player.getInventory().count(item) > 0) isHot = true;
        if (isHot && countOfInARowCalls==0) {
            sendMessage(player, Config.getLang().getString("lang.effect.primitiveHeating.message.get"));
            giveEffect(player, StatusEffects.RESISTANCE);
            return countOfInARowCalls+1;
        }
        else if (!isHot && countOfInARowCalls>0) {
            sendMessage(player, Config.getLang().getString("lang.effect.primitiveHeating.message.remove"));
            removeEffect(player, StatusEffects.RESISTANCE);
        }
        else if (countOfInARowCalls > 0) {
            return countOfInARowCalls+1;
        }
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {
        removeEffect(player, StatusEffects.RESISTANCE);
    }
}
