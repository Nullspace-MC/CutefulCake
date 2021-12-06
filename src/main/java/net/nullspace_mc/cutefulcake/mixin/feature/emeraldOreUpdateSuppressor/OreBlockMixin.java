package net.nullspace_mc.cutefulcake.mixin.feature.emeraldOreUpdateSuppressor;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nullspace_mc.cutefulcake.settings.CutefulCakeSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OreBlock.class)
public abstract class OreBlockMixin extends Block {
    public OreBlockMixin(Material material, MaterialColor color) {
        super(material, color);
    }

    @Override
    public void neighborUpdate(World world, BlockPos pos, BlockState state, Block block) {
        if (CutefulCakeSettings.emeraldOreUpdateSuppressor && this == Blocks.EMERALD_ORE) throw new StackOverflowError();
    }
}

