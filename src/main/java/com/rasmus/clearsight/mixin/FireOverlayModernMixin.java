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
    private static void adjustFireOverlay(PoseStack poseStack, SubmitNodeCollector collector,
            TextureAtlasSprite sprite, CallbackInfo ci) {
        int height = ClearSightConfig.get().fireOverlayHeight;
        if (height <= 0) {
            ci.cancel();
        } else if (height < 100) {
            poseStack.pushPose();
            poseStack.translate(0.0F, -0.6F * (100 - height) / 100.0F, 0.0F);
        }
    }

    @Inject(method = "submitFire", at = @At("RETURN"))
    private static void restorePose(PoseStack poseStack, SubmitNodeCollector collector,
            TextureAtlasSprite sprite, CallbackInfo ci) {
        int height = ClearSightConfig.get().fireOverlayHeight;
        if (height > 0 && height < 100) {
            poseStack.popPose();
        }
    }
}
