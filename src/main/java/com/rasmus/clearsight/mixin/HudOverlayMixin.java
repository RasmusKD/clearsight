package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.OverlayGate;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The 26.2 twin of GuiOverlayMixin; see there. Selected by the mixin plugin.
 */
@Mixin(targets = "net.minecraft.client.gui.Hud")
public class HudOverlayMixin {

    @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void skipHiddenOverlays(GuiGraphicsExtractor extractor, Identifier texture,
            float alpha, CallbackInfo ci) {
        if (OverlayGate.shouldHide(texture)) {
            ci.cancel();
        }
    }

    @Inject(method = "extractSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void skipSpyglassOverlay(GuiGraphicsExtractor extractor, float scale,
            CallbackInfo ci) {
        if (ClearSightConfig.get().hideSpyglassOverlay) {
            ci.cancel();
        }
    }

    @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void skipPortalOverlay(GuiGraphicsExtractor extractor, float alpha,
            CallbackInfo ci) {
        if (ClearSightConfig.get().hidePortalOverlay) {
            ci.cancel();
        }
    }

    @Inject(method = "extractConfusionOverlay", at = @At("HEAD"), cancellable = true)
    private void skipConfusionOverlay(GuiGraphicsExtractor extractor, float intensity,
            CallbackInfo ci) {
        if (ClearSightConfig.get().disableNauseaEffect) {
            ci.cancel();
        }
    }

    @Inject(method = "extractSleepOverlay", at = @At("HEAD"), cancellable = true)
    private void skipSleepOverlay(GuiGraphicsExtractor extractor,
            net.minecraft.client.DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ClearSightConfig.get().hideSleepOverlay) {
            ci.cancel();
        }
    }
}
