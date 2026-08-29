package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The fading field only exists on the one title screen the game builds
 * after startup, and extractRenderState is its only consumer. Clearing it
 * at constructor TAIL means the fade never starts and every widget keeps
 * its default full alpha; the screen instance itself is untouched, so mods
 * that identify the title screen by class still see the real one.
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Shadow
    private boolean fading;

    @Inject(method = "<init>(ZLnet/minecraft/client/gui/components/LogoRenderer;)V",
            at = @At("TAIL"))
    private void skipTitleFade(boolean fadeIn, LogoRenderer logoRenderer, CallbackInfo ci) {
        if (ClearSightConfig.get().hideTitleFade) {
            this.fading = false;
        }
    }
}
