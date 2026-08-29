package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.ClearSightFog;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    /**
     * The nether's thick fog is this same environment fed shorter
     * FOG_START/END_DISTANCE attributes by the dimension and its biomes
     * (basalt deltas shortest), so clearing the environmental planes here
     * covers every nether biome in one place.
     */
    @Inject(method = "setupFog", at = @At("TAIL"))
    private void clearNetherFog(FogData fogData, Camera camera, ClientLevel level,
            float renderDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ClearSightConfig.get().clearNetherFog && level.dimension() == Level.NETHER) {
            ClearSightFog.clearEnvironmental(fogData);
        }
    }
}
