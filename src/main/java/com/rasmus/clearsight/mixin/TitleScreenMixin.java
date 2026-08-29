package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The fading field only exists on the one title screen the game builds
 * after startup, and extractRenderState is its only consumer. The slider
 * scales that fade's duration; at 0 the ctor TAIL clears the field before
 * the first frame instead, because a scaled fade still renders one
 * near-invisible widget frame while the timestamp is being stamped. The
 * screen instance itself is untouched either way, so mods that identify
 * the title screen by class still see the real one.
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Shadow
    private boolean fading;

    @Inject(method = "<init>(ZLnet/minecraft/client/gui/components/LogoRenderer;)V",
            at = @At("TAIL"))
    private void skipTitleFade(boolean fadeIn, LogoRenderer logoRenderer, CallbackInfo ci) {
        if (ClearSightConfig.get().titleFadeTime == 0) {
            this.fading = false;
        }
    }

    @ModifyConstant(method = "extractRenderState", constant = @Constant(floatValue = 2000.0F))
    private float scaleTitleFade(float vanilla) {
        return Math.max(1.0F, vanilla * ClearSightConfig.get().titleFadeTime / 100.0F);
    }
}
