package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class Revitalized extends EffectObject {
    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.revitalized.message.trigger");
        this.isGood = true;
        this.weathers = Collections.singletonList(Weather.BEAUTIFUL);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls) {
        if (player.getServerWorld().getTimeOfDay() < Config.getConfig().getLong("conf.time.day.end")-1) {
            sendMessage(player, Config.getLang().getString("lang.effect.revitalized.message.get"));
            effect(player, StatusEffects.REGENERATION, 0);
            return countOfInARowCalls+1;
        }
        else if (player.getServerWorld().getTimeOfDay() == Config.getConfig().getLong("conf.time.day.end")) {
            sendMessage(player, Config.getLang().getString("lang.effect.revitalized.message.remove"));
            removeEffect(player, StatusEffects.REGENERATION);
        }
        return 0;
    }
}
