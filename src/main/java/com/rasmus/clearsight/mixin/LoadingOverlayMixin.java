package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Backdating vanilla's own fade timestamps is the whole mechanism: the fade
 * math sees an animation that already finished, so the overlay draws at
 * full alpha from the first frame, the one-second minimum hold is already
 * satisfied, and vanilla's own tick error handling and removal branch run
 * untouched. Nothing here closes or removes anything itself.
 */
@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {

    @Shadow
    @Final
    private boolean fadeIn;
    @Shadow
    private long fadeInStart;
    @Shadow
    private long fadeOutStart;

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void skipFadeIn(GuiGraphicsExtractor extractor, int width, int height,
            float partialTick, CallbackInfo ci) {
        if (ClearSightConfig.get().instantResourceReload && this.fadeIn
                && (this.fadeInStart == -1L || Util.getMillis() - this.fadeInStart < 1000L)) {
            this.fadeInStart = Util.getMillis() - 1000L;
        }
    }

    /**
     * TAIL: vanilla's tick body must run in full first; it routes reload
     * errors into the pack rollback path and stamps fadeOutStart.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void skipFadeOut(CallbackInfo ci) {
        if (ClearSightConfig.get().instantResourceReload && this.fadeOutStart > -1L) {
            this.fadeOutStart = Util.getMillis() - 2000L;
        }
    }
}
