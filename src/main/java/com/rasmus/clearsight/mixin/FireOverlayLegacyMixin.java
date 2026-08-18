package com.rasmus.clearsight.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The first-person flame overlay while burning, on 26.1 where the helper
 * is named renderFire. 26.2 renamed it submitFire; the mixin plugin applies
 * whichever matches. Fire on entities in the world is untouched, so you
 * still see that you and others burn.
 */
@Mixin(ScreenEffectRenderer.class)
public class FireOverlayLegacyMixin {

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void skipFireOverlay(PoseStack poseStack, MultiBufferSource bufferSource,
            TextureAtlasSprite sprite, CallbackInfo ci) {
        if (ClearSightConfig.get().hideFireOverlay) {
            ci.cancel();
        }
    }
}
