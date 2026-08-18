package com.rasmus.clearsight.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The 26.2 twin of FireOverlayLegacyMixin; see there.
 */
@Mixin(ScreenEffectRenderer.class)
public class FireOverlayModernMixin {

    @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
    private static void skipFireOverlay(PoseStack poseStack, SubmitNodeCollector collector,
            TextureAtlasSprite sprite, CallbackInfo ci) {
        if (ClearSightConfig.get().hideFireOverlay) {
            ci.cancel();
        }
    }
}
