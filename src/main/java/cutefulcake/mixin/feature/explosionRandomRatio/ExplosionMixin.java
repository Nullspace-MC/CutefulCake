package cutefulcake.mixin.feature.explosionRandomRatio;

import cutefulcake.settings.CutefulCakeSettings;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow @Final private World field_229;

    @Redirect(
            method = "collectBlocksAndDamageEntities",
            at = @At(
                    value = "INVOKE",
                    target = "java/util/Random.nextFloat()F"
            )
    )
    private float setValueOfRayStrength(Random instance) {
        float value = instance.nextFloat();
        if (CutefulCakeSettings.explosionRandomRatio != -1.0F) value = CutefulCakeSettings.explosionRandomRatio;
        return value;
    }
}
