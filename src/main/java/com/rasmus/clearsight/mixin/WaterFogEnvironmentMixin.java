package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.ClearSightFog;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The submerged fog environments are the only color source for their fog
 * type, so they must stay applicable (removing them crashes the renderer
 * with "No color source environment found"). Instead the fog distances are
 * pushed out of sight while the color stays.
 */
@Mixin(WaterFogEnvironment.class)
public class WaterFogEnvironmentMixin {

    @Inject(method = "setupFog", at = @At("TAIL"))
    private void pushFogAway(FogData fogData, Camera camera, ClientLevel level,
            float renderDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ClearSightConfig.get().clearWaterFog) {
            ClearSightFog.clear(fogData);
        }
    }
}
