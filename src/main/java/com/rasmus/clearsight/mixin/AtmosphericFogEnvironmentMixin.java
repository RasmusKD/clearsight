package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Rain also dims the whole world. With the weather hidden, the gloom would
 * give it away, so hiding weather skips the darkening too and the world
 * keeps its clear-day colors.
 */
@Mixin(AtmosphericFogEnvironment.class)
public class AtmosphericFogEnvironmentMixin {

    @Inject(method = "applyWeatherDarken", at = @At("HEAD"), cancellable = true)
    private static void skipWeatherDarken(int color, float rainLevel, float thunderLevel,
            CallbackInfoReturnable<Integer> cir) {
        if (ClearSightConfig.get().hideWeather) {
            cir.setReturnValue(color);
        }
    }
}
