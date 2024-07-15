package ru.kochkaev.Seasons4Fabric.challenge;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sweating extends ChallengeObject {

    public Sweating() {
        super(Config.getLang().getString("lang.effect.sweating.message.trigger"), Collections.singletonList(Weather.getWeatherByID("HOT")), false);
    }

    private static final List<Block> waters = Arrays.asList(Blocks.WATER, Blocks.WATER_CAULDRON);

    @Override
    public void register() {
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        boolean isFullArmor = true;
        for (ItemStack item : player.getArmorItems()) isFullArmor = item.getItem() != Items.AIR && isFullArmor;
        if (isFullArmor) {
            if (!(player.getSteppingBlockState().getBlock() == Blocks.WATER || waters.contains(player.getBlockStateAtPos().getBlock()))) {
                if (countOfInARowCalls == 0)
                    sendMessage(player, Config.getLang().getString("lang.effect.sweating.message.get"));
                else if (countOfInARowCalls % ticksPerAction == 0) {
                    damageHot(player);
                    spawnParticles(player, ParticleTypes.SMALL_FLAME, false, 0, 10);
                }
                if (countOfInARowCalls == ticksPerAction) giveEffect(player, StatusEffects.MINING_FATIGUE);
                return countOfInARowCalls + 1;
            }
            else if (countOfInARowCalls > 0) {
                player.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE);
                spawnParticles(player, ParticleTypes.SMOKE, false, 0, 10);
            }
        }
        else if (countOfInARowCalls > 0) {
            sendMessage(player, Config.getLang().getString("lang.effect.sweating.message.remove"));
            return -1 - ticksPerAction;
        }
        else if (countOfInARowCalls == -1) {
            removeEffect(player, StatusEffects.MINING_FATIGUE);
            spawnParticles(player, ParticleTypes.HAPPY_VILLAGER, false, 1, 10);
        }
        else if (countOfInARowCalls < -1) return countOfInARowCalls + 1;
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {
        removeEffect(player, StatusEffects.MINING_FATIGUE);
    }
}
