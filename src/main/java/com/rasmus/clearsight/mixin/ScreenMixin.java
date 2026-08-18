package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.ClearSightMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The darkening behind in-game screens comes down two paths: container
 * screens (isInGameUi=true) draw a transparent gradient, everything else
 * (pause menu, signs) draws blur + menu background. Both are skipped for
 * hidden categories, but only while a level is loaded, so title screens
 * keep their panorama and blur.
 */
@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(method = "extractTransparentBackground", at = @At("HEAD"), cancellable = true)
    private void skipMenuDarkening(GuiGraphicsExtractor extractor, CallbackInfo ci) {
        if (clearsight$hideInGame()) {
            ci.cancel();
        }
    }

    @Inject(method = "extractBlurredBackground", at = @At("HEAD"), cancellable = true)
    private void skipMenuBlur(GuiGraphicsExtractor extractor, CallbackInfo ci) {
        if (clearsight$hideInGame()) {
            ci.cancel();
        }
    }

    @Inject(method = "extractMenuBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
            at = @At("HEAD"), cancellable = true)
    private void skipMenuBackground(GuiGraphicsExtractor extractor, CallbackInfo ci) {
        if (clearsight$hideInGame()) {
            ci.cancel();
        }
    }

    private boolean clearsight$hideInGame() {
        return Minecraft.getInstance().level != null
                && ClearSightMenus.shouldHideDarkening((Screen) (Object) this);
    }
}
