package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.ClearSightFog;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The render-distance planes have to be cleared here: FogRenderer rewrites
 * them after every fog environment has run, so the environment-level hooks
 * cannot reach them. RETURN is after that rewrite. Blindness and darkness
 * are environmental fog and stay untouched.
 */
@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void clearRenderDistanceFog(Camera camera, int renderDistanceChunks,
            DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level,
            CallbackInfoReturnable<FogData> cir) {
        if (ClearSightConfig.get().clearDistanceFog) {
            ClearSightFog.clearRenderDistance(cir.getReturnValue());
        }
    }
}
