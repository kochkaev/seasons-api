package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;
import java.util.Random;

public class HoldOntoYourHat extends ChallengeObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.holdOntoYourHat.message.trigger");
        this.weathers = Collections.singletonList(Weather.BREEZY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        Main.getLogger().info("HoldOntoYourHat logic");
        boolean haveLeatherHelmet = false;
        ItemStack helmet = Items.LEATHER_HELMET.getDefaultStack();
        for (ItemStack item : player.getArmorItems()){
            if (item.getItem() == Items.LEATHER_HELMET) {
                haveLeatherHelmet = true;
                helmet = item;
                break;
            }
        }
        Main.getLogger().info(String.valueOf(haveLeatherHelmet));
        if (haveLeatherHelmet && new Random().nextInt(100) <= 10) {
            player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), helmet));
            //player.getInventory().offerOrDrop(helmet);
            player.getInventory().removeOne(helmet);
            sendMessage(player, Config.getLang().getString("lang.effect.holdOntoYourHat.message.get"));
        }
        return 0;
    }
}
