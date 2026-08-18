package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Some boss bars ask the game to darken the whole world while the boss is
 * alive. The bar itself stays, only the dimming goes.
 */
@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {

    @Inject(method = "shouldDarkenScreen", at = @At("HEAD"), cancellable = true)
    private void skipBossDarkening(CallbackInfoReturnable<Boolean> cir) {
        if (ClearSightConfig.get().hideBossDarkening) {
            cir.setReturnValue(false);
        }
    }
}
