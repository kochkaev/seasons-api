package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;

public class Frostbite extends ChallengeObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.frostbite.message.trigger");
        this.weathers = Arrays.asList(Weather.SNOWY, Weather.FREEZING);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        boolean doNotWearArmor = false;
        for (ItemStack item : player.getArmorItems()) doNotWearArmor = item.getItem() == Items.AIR || (doNotWearArmor);
        Main.getLogger().info(String.valueOf(doNotWearArmor));
        if (player.getHealth()>1 && doNotWearArmor) {
            Main.getLogger().info(String.valueOf(0));
            player.slowMovement(player.getBlockStateAtPos(), new Vec3d(0.8999999761581421, 1.5, 0.8999999761581421));
            if (countOfInARowCalls<ticksPerAction) {
                player.setInPowderSnow(true);
                Main.getLogger().info(String.valueOf(1));
                return countOfInARowCalls+1;
            }
            else {
                Main.getLogger().info(String.valueOf(2));
                damage(player);
                return 0;
            }
        }
        else if (countOfInARowCalls>0) {
            Main.getLogger().info(String.valueOf(3));
            player.setInPowderSnow(false);
        }
        return 0;
    }
}
