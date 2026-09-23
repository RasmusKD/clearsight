package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Rain fades the sun and moon out. With the weather hidden that left a
 * clear sky with nothing in it, so hiding weather keeps them at full
 * strength.
 */
@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void keepSunAndMoon(ClientLevel level, float partialTicks, Camera camera,
            SkyRenderState state, CallbackInfo ci) {
        if (ClearSightConfig.get().hideWeather) {
            state.rainBrightness = 1.0F;
        }
    }
}
