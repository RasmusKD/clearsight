package com.rasmus.clearsight.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**
     * The totem (and any item activation) animation covers the entire screen
     * for a second, exactly when you most need to see what almost killed
     * you. The sound and the hearts already carry the information.
     */
    @Inject(method = "displayItemActivation", at = @At("HEAD"), cancellable = true)
    private void skipItemActivation(ItemStack stack, CallbackInfo ci) {
        if (ClearSightConfig.get().hideTotemAnimation) {
            ci.cancel();
        }
    }

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void skipHurtCam(CameraRenderState renderState, PoseStack poseStack,
            CallbackInfo ci) {
        if (ClearSightConfig.get().hideHurtCam) {
            ci.cancel();
        }
    }
}
