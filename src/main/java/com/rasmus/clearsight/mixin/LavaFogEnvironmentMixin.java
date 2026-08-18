package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.ClearSightFog;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.LavaFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFogEnvironment.class)
public class LavaFogEnvironmentMixin {

    @Inject(method = "setupFog", at = @At("TAIL"))
    private void pushFogAway(FogData fogData, Camera camera, ClientLevel level,
            float renderDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ClearSightConfig.get().clearLavaFog) {
            ClearSightFog.clear(fogData);
        }
    }
}
