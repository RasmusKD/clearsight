package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Collapses vanilla's own fade durations instead of skipping code: the
 * fade math, the tick error handling, and the removal branch all run
 * untouched, just against a one-millisecond span, so the overlay closes
 * within a frame of the reload being done. Nothing here closes or removes
 * anything itself.
 */
@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {

    // A divisor of 1 instead of 0 keeps the animation values finite;
    // elapsed milliseconds over 1 blow straight past every fade threshold.
    @ModifyConstant(method = "extractRenderState", constant = @Constant(floatValue = 1000.0F))
    private float collapseFadeOut(float vanilla) {
        return ClearSightConfig.get().instantResourceReload ? 1.0F : vanilla;
    }

    @ModifyConstant(method = "extractRenderState", constant = @Constant(floatValue = 500.0F))
    private float collapseFadeIn(float vanilla) {
        return ClearSightConfig.get().instantResourceReload ? 1.0F : vanilla;
    }

    @ModifyConstant(method = "isReadyToFadeOut", constant = @Constant(longValue = 1000L))
    private long collapseMinimumHold(long vanilla) {
        return ClearSightConfig.get().instantResourceReload ? 0L : vanilla;
    }
}
