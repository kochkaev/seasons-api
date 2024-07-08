package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import javax.swing.text.html.parser.Entity;
import java.util.Collections;
import java.util.Random;

public class HoldOntoYourHat extends EffectObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.holdOntoYourHat.message.trigger");
        this.weathers = Collections.singletonList(Weather.BREEZY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (player.getArmorItems().equals(Items.LEATHER_HELMET) && new Random().nextInt(100) < 25) {
            ItemStack helmet = player.getArmorItems().iterator().next();
            while (helmet.getItem() != Items.LEATHER_HELMET) helmet = player.getArmorItems().iterator().next();
            player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), helmet));
            player.getInventory().armor.remove(helmet);
            sendMessage(player, Config.getLang().getString("lang.effect.holdOntoYourHat.message.get"));
        }
        return 0;
    }
}
