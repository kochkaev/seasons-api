package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SolderingIron extends ChallengeObject {

    private static final List<ItemStack> items = Arrays.asList(Items.BUCKET.getDefaultStack(), Items.IRON_INGOT.getDefaultStack(), Items.IRON_BLOCK.getDefaultStack(),
            Items.IRON_DOOR.getDefaultStack(), Items.IRON_HELMET.getDefaultStack(), Items.IRON_CHESTPLATE.getDefaultStack(), Items.IRON_LEGGINGS.getDefaultStack(),
            Items.IRON_BOOTS.getDefaultStack(), Items.ANVIL.getDefaultStack(), Items.IRON_NUGGET.getDefaultStack(), Items.IRON_BARS.getDefaultStack(), Items.IRON_TRAPDOOR.getDefaultStack(),
            Items.CHAINMAIL_HELMET.getDefaultStack(), Items.CHAINMAIL_CHESTPLATE.getDefaultStack(), Items.CHAINMAIL_LEGGINGS.getDefaultStack(),
            Items.CHAINMAIL_BOOTS.getDefaultStack(), Items.WATER_BUCKET.getDefaultStack(), Items.LAVA_BUCKET.getDefaultStack());
    
    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.solderingIron.message.trigger");
        this.weathers = Collections.singletonList(Weather.SCORCHING);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (items.contains(player.getActiveItem())) {
            if (countOfInARowCalls == 0) sendMessage(player, Config.getLang().getString("lang.effect.solderingIron.message.get"));
            return countOfInARowCalls+1;
        }
        else if (countOfInARowCalls == ticksPerAction) {
            damageHot(player);
        }
        return 0;
    }
}
