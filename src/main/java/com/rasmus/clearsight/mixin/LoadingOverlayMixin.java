package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Scales vanilla's own fade durations instead of skipping code: the fade
 * math, the tick error handling, and the removal branch all run untouched,
 * just against shorter time spans. 100 = vanilla, 0 = the overlay closes
 * within a frame or two of the reload being done. The boot overlay
 * (fadeIn=false, no fade-in
 * and no minimum hold in vanilla) and manual reloads (fadeIn=true) get
 * separate sliders, told apart by the fadeIn field.
 */
@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {

    @Shadow
    @Final
    private boolean fadeIn;

    private float clearsight$scale() {
        ClearSightConfig config = ClearSightConfig.get();
        return (this.fadeIn ? config.reloadFadeTime : config.bootFadeTime) / 100.0F;
    }

    // A divisor of at least 1 keeps the animation values finite; elapsed
    // milliseconds over 1 blows straight past every fade threshold.
    @ModifyConstant(method = "extractRenderState", constant = @Constant(floatValue = 1000.0F))
    private float scaleFadeOut(float vanilla) {
        return Math.max(1.0F, vanilla * clearsight$scale());
    }

    @ModifyConstant(method = "extractRenderState", constant = @Constant(floatValue = 500.0F))
    private float scaleFadeIn(float vanilla) {
        return Math.max(1.0F, vanilla * clearsight$scale());
    }

    @ModifyConstant(method = "isReadyToFadeOut", constant = @Constant(longValue = 1000L))
    private long scaleMinimumHold(long vanilla) {
        return (long) (vanilla * clearsight$scale());
    }
}
