package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import ru.kochkaev.Seasons4Fabric.IFuncRet;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;

public class Frostbite extends ChallengeObject {

    public Frostbite() {
        super(Config.getLang().getString("lang.effect.frostbite.message.trigger"), Arrays.asList(Weather.SNOWY, Weather.FREEZING), true);
    }

    private IFuncRet task;

    @Override
    public void register() {
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        boolean doNotWearArmor = false;
        for (ItemStack item : player.getArmorItems()) doNotWearArmor = item.getItem() == Items.AIR || (doNotWearArmor);
        if (doNotWearArmor) {
            if (countOfInARowCalls==0) {
                player.setInPowderSnow(true);
                task = giveFrozen(player);
                return countOfInARowCalls+1;
            }
            if (countOfInARowCalls==1) {
                return countOfInARowCalls+1;
            }
            else if (countOfInARowCalls%ticksPerAction == 0) {
                damageCold(player);
                return 1;
            }
            return countOfInARowCalls+1;
        }
        else if (countOfInARowCalls>0) {
            player.setInPowderSnow(false);
            removeFrozen(task);
        }
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {
        player.setInPowderSnow(false);
        removeFrozen(task);
    }
}
