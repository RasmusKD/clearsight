package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.OverlayGate;
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
}
