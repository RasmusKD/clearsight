package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides falling rain and snow client-side. The weather itself still
 * happens (crops grow, mobs burn or not), you just do not stare through it.
 */
@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void skipWeatherRendering(Vec3 cameraPosition, WeatherRenderState renderState,
            CallbackInfo ci) {
        if (ClearSightConfig.get().hideFallingWeather()) {
            ci.cancel();
        }
    }
}
