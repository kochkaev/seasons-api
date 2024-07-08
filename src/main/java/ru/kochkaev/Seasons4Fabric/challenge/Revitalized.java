package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class Revitalized extends ChallengeObject {
    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.revitalized.message.trigger");
        this.weathers = Collections.singletonList(Weather.BEAUTIFUL);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (player.getServerWorld().getTimeOfDay() < Config.getConfig().getLong("conf.time.day.end")-1) {
            sendMessage(player, Config.getLang().getString("lang.effect.revitalized.message.get"));
            giveEffect(player, StatusEffects.REGENERATION);
            return countOfInARowCalls+1;
        }
        else if (player.getServerWorld().getTimeOfDay() == Config.getConfig().getLong("conf.time.day.end")) {
            sendMessage(player, Config.getLang().getString("lang.effect.revitalized.message.remove"));
            removeEffect(player, StatusEffects.REGENERATION);
        }
        return 0;
    }
}
