package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.OverlayGate;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 26.1 names the HUD class Gui; 26.2 renamed it Hud. The mixin plugin
 * applies exactly one of this class and HudOverlayMixin depending on the
 * running version.
 */
@Mixin(targets = "net.minecraft.client.gui.Gui")
public class GuiOverlayMixin {

    @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void skipHiddenOverlays(GuiGraphicsExtractor extractor, Identifier texture,
            float alpha, CallbackInfo ci) {
        if (OverlayGate.shouldHide(texture)) {
            ci.cancel();
        }
    }
}
