package net.nullspace_mc.cutefulcake.mixin.core;

import net.nullspace_mc.cutefulcake.CutefulCake;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    // in case another mod changes level name during runtime ofc
    @Inject(method = "setupWorld(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/level/LevelGeneratorType;Ljava/lang/String;)V", at = @At("HEAD"))
    private void onSetupWorld(CallbackInfo ci) {
        CutefulCake.initializeCakeServer();
    }


    @Inject(method = "shutdown", at = @At(value = "HEAD"))
    private void onTickServer(CallbackInfo ci) {
        CutefulCake.tickCakeServer();
    }
}
