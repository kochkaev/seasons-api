package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class Frostbite extends EffectObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.frostbite.message.trigger");
        this.isGood = true;
        this.weathers = Collections.singletonList(Weather.SNOWY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls) {
        if (player.getHealth()>1 && (player.getArmorItems().equals(Items.AIR))) {
            player.slowMovement(player.getBlockStateAtPos(), new Vec3d(0.8999999761581421, 1.5, 0.8999999761581421));
            if (countOfInARowCalls<10) {
                player.setInPowderSnow(true);
                return countOfInARowCalls+1;
            }
            else {
                damage(player);
                return 0;
            }
        }
        else if (countOfInARowCalls>0) {
            player.setInPowderSnow(true);
        }
        return 0;
    }
}
