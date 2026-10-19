package com.rasmus.clearsight.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The 26.3 twin of the main GameRendererMixin: the item activation moved to
 * LocalPlayer (LocalPlayerItemActivationMixin) and renderLevel reads the
 * portal intensity from the extracted PlayerRenderState instead of the
 * player.
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void skipHurtCam(CameraRenderState renderState, PoseStack poseStack,
            CallbackInfo ci) {
        if (ClearSightConfig.get().hideHurtCam) {
            ci.cancel();
        }
    }

    /**
     * Standing in a nether portal drives the same spinning screen warp as
     * nausea; renderLevel takes the larger of the two intensities. Zeroing
     * the portal read removes the warp and leaves the nausea one untouched.
     */
    @Redirect(method = "renderLevel", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/renderer/state/level/PlayerRenderState;portalEffectIntensity:F",
            opcode = Opcodes.GETFIELD))
    private float zeroPortalSpin(PlayerRenderState state) {
        return ClearSightConfig.get().hidePortalOverlay ? 0.0F : state.portalEffectIntensity;
    }
}
