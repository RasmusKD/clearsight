package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.server.level.ParticleStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ground splashes and rain sound on 26.1, where they live on the weather
 * renderer. 26.2 moved them into ClientLevel.tickWeatherEffects; the mixin
 * plugin applies whichever matches the running version.
 */
@Mixin(WeatherEffectRenderer.class)
public class WeatherTickLegacyMixin {

    @Inject(method = "tickRainParticles", at = @At("HEAD"), cancellable = true)
    private void skipRainParticles(ClientLevel level, Camera camera, int ticks,
            ParticleStatus particleStatus, int intensity, CallbackInfo ci) {
        if (ClearSightConfig.get().hideAllWeather()) {
            ci.cancel();
        }
    }
}
