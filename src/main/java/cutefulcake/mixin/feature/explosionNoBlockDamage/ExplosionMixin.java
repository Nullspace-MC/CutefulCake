package cutefulcake.mixin.feature.explosionNoBlockDamage;

import cutefulcake.settings.CutefulCakeSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow @Final private List<BlockPos> affectedBlocks;

    @Inject(
            method = "collectBlocksAndDamageEntities",
            at = @At(value = "TAIL")
    )
    private void clearAffectedBlocks(CallbackInfo ci) {
        if (CutefulCakeSettings.explosionNoBlockDamage) this.affectedBlocks.clear();
    }
}
