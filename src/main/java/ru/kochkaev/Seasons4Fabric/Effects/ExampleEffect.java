package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class ExampleEffect extends EffectObject {

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect..message.trigger");
        this.weathers = Collections.singletonList(Weather.SNOWY);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        if (true) {
            return countOfInARowCalls+1;
        }
        else if (false) {

        }
        return 0;
    }
}
