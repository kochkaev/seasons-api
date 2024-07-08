package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.object.EventObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Collections;

public class Icy extends ChallengeObject {

    private EventObject onBlockChange;

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.icy.message.trigger");
        this.weathers = Collections.singletonList(Weather.FREEZING);
        onBlockChange = registerOnEventMethod("ON_BLOCK_CHANGE", this::onBlockChange);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        return 0;
    }

    public void onBlockChange(Object... args)  {
        BlockState newBlock = (BlockState) ((Object[]) args[0])[2];

        if(newBlock.getBlock() == Blocks.WATER) onBlockChange.returnValue(Blocks.ICE.getDefaultState());
        //sendMessage(Config.getLang().getString(player, "lang.effect.icy.message.get"));
    }
}
