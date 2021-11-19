package net.nullspace_mc.cutefulcake.mixin.tick;

import net.minecraft.server.MinecraftServer;
import net.nullspace_mc.cutefulcake.CutefulCake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @ModifyConstant(
            method = "run",
            constant = @Constant(longValue = 50L)
    )
    private long ifTickWarpingDoNotWait(long l) {
        return CutefulCake.isTickWarping ? 0L : l;
    }
}
