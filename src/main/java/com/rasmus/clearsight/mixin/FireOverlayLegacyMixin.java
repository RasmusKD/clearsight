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
 * The first-person flame while burning, on 26.1 where the helper is named
 * renderFire (26.2: submitFire, see FireOverlayModernMixin). Off removes
 * it, low shifts it down so a flicker at the screen edge still says you
 * are burning. World fire on entities is untouched either way.
 */
@Mixin(ScreenEffectRenderer.class)
public class FireOverlayLegacyMixin {

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void adjustFireOverlay(PoseStack poseStack, MultiBufferSource bufferSource,
            TextureAtlasSprite sprite, CallbackInfo ci) {
        int height = ClearSightConfig.get().fireOverlayHeight;
        if (height <= 0) {
            ci.cancel();
        } else if (height < 100) {
            poseStack.pushPose();
            poseStack.translate(0.0F, -0.6F * (100 - height) / 100.0F, 0.0F);
        }
    }

    @Inject(method = "renderFire", at = @At("RETURN"))
    private static void restorePose(PoseStack poseStack, MultiBufferSource bufferSource,
            TextureAtlasSprite sprite, CallbackInfo ci) {
        int height = ClearSightConfig.get().fireOverlayHeight;
        if (height > 0 && height < 100) {
            poseStack.popPose();
        }
    }
}
