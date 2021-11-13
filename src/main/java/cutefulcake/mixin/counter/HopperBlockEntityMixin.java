package cutefulcake.mixin.counter;

import cutefulcake.counter.CounterRegistry;
import cutefulcake.settings.CutefulCakeSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.HopperProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends BlockEntity implements HopperProvider {

    @Shadow private ItemStack[] field_1452;

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/HopperBlockEntity;setCooldown(I)V"
            )
    )
    private void beforeTransfer(CallbackInfo ci) {
        if (CutefulCakeSettings.hopperCounters && isHopperCounter()) {
            for (ItemStack stack : field_1452) {
                if (stack == null) continue;
                CounterRegistry.getCounter(getCounterColor()).addToCounter(stack.getName(), stack.count);
            }
            field_1452 = new ItemStack[]{null, null, null, null, null};
        }
    }

    private boolean isHopperCounter() {
        return this.world.getBlockState(posFacing()).getBlock().getMaterial() == Material.WOOL;
    }

    private BlockPos posFacing() {
        return this.pos.offset(HopperBlock.method_879(this.getDataValue()));
    }

    private String getCounterColor() {
        BlockState blockFacing = this.world.getBlockState(posFacing());
        return DyeColor.getColorById(blockFacing.getBlock().getData(blockFacing)).asString();
    }


    // this is not used at all, just without it legacy fabric crashes lmfao
    @Override
    public World getServerWorld() {
        return world;
    }
}
