package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The totem (and any item activation) animation covers the entire screen
 * for a second, exactly when you most need to see what almost killed
 * you. The sound and the hearts already carry the information. On 26.3 the
 * hook lives on LocalPlayer; before that it was GameRenderer.
 */
@Mixin(LocalPlayer.class)
public class LocalPlayerItemActivationMixin {

    @Inject(method = "displayItemActivation", at = @At("HEAD"), cancellable = true)
    private void skipItemActivation(ItemStack stack, CallbackInfo ci) {
        if (ClearSightConfig.get().hideTotemAnimation) {
            ci.cancel();
        }
    }
}
