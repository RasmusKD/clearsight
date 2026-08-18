package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Leaves drip water during rain on their own, separate from the rain
 * rendering and the ground splashes. Hidden rain with visible drips would
 * be a forest crying for no reason.
 */
@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @Inject(method = "makeDrippingWaterParticles", at = @At("HEAD"), cancellable = true)
    private static void skipRainDrips(Level level, BlockPos pos, RandomSource random,
            BlockState state, BlockPos belowPos, CallbackInfo ci) {
        if (ClearSightConfig.get().hideWeather) {
            ci.cancel();
        }
    }
}
