package ru.kochkaev.Seasons4Fabric.challenge;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import ru.kochkaev.Seasons4Fabric.util.functional.IFuncRet;
import ru.kochkaev.Seasons4Fabric.config.Config;
import ru.kochkaev.Seasons4Fabric.object.ChallengeObject;
import ru.kochkaev.Seasons4Fabric.object.EventObject;
import ru.kochkaev.Seasons4Fabric.service.Task;
import ru.kochkaev.Seasons4Fabric.service.Weather;
import ru.kochkaev.Seasons4Fabric.util.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Icy extends ChallengeObject {

    public Icy() {
        super(Config.getLang().getString("lang.effect.icy.message.trigger"), Collections.singletonList(Weather.getWeatherByID("FREEZING")), true);
    }

    private EventObject onBlockChange;

    @Override
    public void register() {
        onBlockChange = registerOnEventMethod("ON_BLOCK_CHANGE", this::onBlockChange);
    }

    @Override
    public int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) {
        return 0;
    }

    @Override
    public void challengeEnd(ServerPlayerEntity player) {

    }

    public void onBlockChange(List<Object> args)  {
        if (isAllowed()){
            ItemStack item = (ItemStack) args.get(1);
//        Main.getLogger().info("Block");
            if (item.getItem() == Items.WATER_BUCKET) {
                IFuncRet task = (arg) -> {
                    int count = (Integer) arg.getFirst();
                    ServerPlayerEntity player = (ServerPlayerEntity) arg.get(1);
                    BlockPos pos = (BlockPos) arg.get(2);
                    IFuncRet tsk = (IFuncRet) arg.get(3);
                    if (count == 10) {
                        if (player.getServerWorld().getBlockState(pos).getBlock() == Blocks.WATER) {
                            player.getServerWorld().setBlockState(pos, Blocks.ICE.getDefaultState());
                            Message.sendMessage2Player(Config.getLang().getString("lang.effect.icy.message.get"), player);
                            player.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE);
                            player.getServerWorld().spawnParticles(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), 5, pos.getX(), pos.getY(), pos.getZ(), 0.1);
                        }
                        Task.removeTask(tsk);
                        return new ArrayList<>();
                    }
                    return Arrays.asList(count + 1, player, pos, tsk);
                };
                onBlockChange.injectToEnd((arg) -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) arg.getFirst();
                    BlockHitResult hitResult = (BlockHitResult) arg.get(1);
                    BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
                    Task.addTask(task, Arrays.asList(0, player, pos, task));
                });
            }
            if (item.getItem() == Items.BUCKET) {
//            Main.getLogger().info("Bucket");
                IFuncRet task = (arg) -> {
//                Main.getLogger().info("Task");
                    int count = (Integer) arg.getFirst();
                    ServerPlayerEntity player = (ServerPlayerEntity) arg.get(1);
                    BlockPos pos = (BlockPos) arg.get(2);
                    IFuncRet tsk = (IFuncRet) arg.get(3);
                    if (count == 20) {
//                    Main.getLogger().info(player.getInventory().getMainHandStack().toString());
                        if (player.getInventory().getMainHandStack().getItem() == Items.WATER_BUCKET) {
                            player.getInventory().setStack(player.getInventory().selectedSlot, Items.BUCKET.getDefaultStack());
                            Message.sendMessage2Player(Config.getLang().getString("lang.effect.icy.message.remove"), player);
                            player.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0);
                            spawnParticles(player, ParticleTypes.CLOUD, false, 0, 5);
                        }
                        Task.removeTask(tsk);
                        return new ArrayList<>();
                    }
                    return Arrays.asList(count + 1, player, pos, tsk);
                };
                onBlockChange.injectToEnd((arg) -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) arg.getFirst();
                    BlockHitResult hitResult = (BlockHitResult) arg.get(1);
                    BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
                    Block block = player.getServerWorld().getBlockState(pos).getBlock();
//                Main.getLogger().info(block.toString());
                    if (block == Blocks.WATER || block == Blocks.CAULDRON)
                        Task.addTask(task, Arrays.asList(0, player, pos, task));
                });
            }
        }
    }
}
