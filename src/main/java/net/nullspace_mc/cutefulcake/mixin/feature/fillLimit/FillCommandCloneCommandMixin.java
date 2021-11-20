package net.nullspace_mc.cutefulcake.mixin.feature.fillLimit;

import net.minecraft.server.command.CloneCommand;
import net.minecraft.server.command.FillCommand;
import net.nullspace_mc.cutefulcake.settings.CutefulCakeSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({FillCommand.class, CloneCommand.class})
public class FillCommandCloneCommandMixin {
    @ModifyConstant(
            method = "execute",
            constant = @Constant(intValue = 32768)
    )
    private int changeMaxFillLimit(int i) {
        return CutefulCakeSettings.fillLimit == -1 ? Integer.MAX_VALUE : CutefulCakeSettings.fillLimit;
    }
}
