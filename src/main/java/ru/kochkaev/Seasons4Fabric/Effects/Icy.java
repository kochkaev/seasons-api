package ru.kochkaev.Seasons4Fabric.Effects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Objects.EventObject;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.Collections;

public class Icy extends EffectObject {

    private EventObject onBlockChange;

    @Override
    public void register() {
        this.triggerMessage = Config.getLang().getString("lang.effect.icy.message.trigger");
        this.weathers = Collections.singletonList(Weather.SNOWY);
        onBlockChange = registerOnEventMethod("ON_BLOCK_CHANGE", this::onBlockChange);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        return 0;
    }

    public void onBlockChange(Object... args)  {
        BlockPos pos = (BlockPos) args[0];
        BlockState oldBlock = (BlockState) args[1];
        BlockState newBlock  =  (BlockState) args[2];

        if(newBlock.getBlock() == Blocks.WATER) onBlockChange.returnValue(Blocks.ICE.getDefaultState());
        //sendMessage(Config.getLang().getString(player, "lang.effect.icy.message.get"));
    }
}
