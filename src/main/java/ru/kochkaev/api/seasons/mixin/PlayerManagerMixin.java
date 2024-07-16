package ru.kochkaev.api.seasons.mixin;

import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

//    @Inject(method = "onPlayerConnect", at=@At("HEAD"))
//    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
//        ChallengesTicker.addPlayer(player);
//    }

}
