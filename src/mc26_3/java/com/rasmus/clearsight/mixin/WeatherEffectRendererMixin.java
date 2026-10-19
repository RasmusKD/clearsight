package com.rasmus.clearsight.mixin;

import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides falling rain and snow client-side. The weather itself still
 * happens (crops grow, mobs burn or not), you just do not stare through it.
 * On 26.3 the public render and renderOit both draw through this private
 * overload, so one hook covers both.
 */
@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/renderer/state/level/WeatherRenderState;"
            + "Lcom/mojang/renderpearl/api/commands/RenderPass;"
            + "Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V",
            at = @At("HEAD"), cancellable = true)
    private void skipWeatherRendering(WeatherRenderState renderState, RenderPass renderPass,
            RenderPipeline pipeline, CallbackInfo ci) {
        if (ClearSightConfig.get().hideWeather) {
            ci.cancel();
        }
    }
}
