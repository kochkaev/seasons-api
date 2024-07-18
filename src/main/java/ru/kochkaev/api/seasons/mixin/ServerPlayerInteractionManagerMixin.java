package ru.kochkaev.api.seasons.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kochkaev.api.seasons.object.EventObject;
import ru.kochkaev.api.seasons.service.Event;

import java.util.Arrays;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Unique
    private static final EventObject onBlockChangeEvent = Event.getEventByID("ON_BLOCK_CHANGE");

    @Inject(at = @At("HEAD"), method = "interactBlock"/*, cancellable = true*/)
    public void interactBlock(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        onBlockChangeEvent.onEvent(Arrays.asList(player, stack));
        if (onBlockChangeEvent.isCancelledAndReset()) {
            cir.cancel();
        }
    }

    @ModifyVariable(at = @At("HEAD"), method = "interactBlock", ordinal = 0, argsOnly = true)
    public ItemStack interactBlockEvent(ItemStack stack) {
        if (onBlockChangeEvent.isReturnedAndReset()) return (ItemStack) onBlockChangeEvent.getReturned();
        return stack;
    }

    @Inject(method = "interactBlock", at = @At("RETURN"))
    public void interactBlockRet(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        onBlockChangeEvent.invokeInjectedAndReset(Arrays.asList(player, hitResult));
    }

}
