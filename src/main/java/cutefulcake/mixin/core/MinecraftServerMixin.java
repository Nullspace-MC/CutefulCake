package cutefulcake.mixin.core;

import cutefulcake.settings.SettingsManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    // in case another mod changes level name during runtime ofc
    @Inject(method = "setLevelName", at = @At("TAIL"), cancellable = true)
    private void whenLevelNameIsDefined(String levelName, CallbackInfo ci) {
        try {
            SettingsManager.setupSettingsManager();
        } catch (IOException e) {
            e.printStackTrace();
            ci.cancel();
        }
    }
}
