package net.nullspace_mc.cutefulcake.mixin.feature.fillUpdates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.command.CloneCommand;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nullspace_mc.cutefulcake.settings.CutefulCakeSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FillCommand.class, CloneCommand.class, SetBlockCommand.class})
public class FillCommandCloneCommandSetBlockCommandMixin {
    @Redirect(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;method_362(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V")
    )
    private void suppressUpdatesOnFillCloneSetblockCommand(World world, BlockPos blockPos, Block block) {
        if (CutefulCakeSettings.fillUpdates) world.method_362(blockPos, block);
    }

    @Redirect(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z")
    )
    private boolean alterFlagToRemoveBlockUpdatesOnFillCloneSetblockCommand(World world, BlockPos pos, BlockState state, int flags) {
        return world.setBlockState(pos, state, CutefulCakeSettings.fillUpdates ? flags : 2);
    }
}
