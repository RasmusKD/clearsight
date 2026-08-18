package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The 26.2 twin of WeatherTickLegacyMixin; see there.
 */
@Mixin(targets = "net.minecraft.client.multiplayer.ClientLevel")
public class WeatherTickModernMixin {

    @Inject(method = "tickWeatherEffects", at = @At("HEAD"), cancellable = true)
    private void skipRainParticles(CallbackInfo ci) {
        if (ClearSightConfig.get().hideWeather) {
            ci.cancel();
        }
    }
}
