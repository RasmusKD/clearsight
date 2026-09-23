package com.rasmus.clearsight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Rain and thunder also reach the world through environment attributes:
 * a gray sky, darker fog and clouds, less sky light, no stars and a dull
 * sunset (WeatherAttributes.RAIN and THUNDER). They read the weather
 * through this anonymous WeatherAccess, so with the weather hidden the
 * client's level reports none here and every one of them stays at its
 * clear-weather value. Only the client level: the server, integrated one
 * included, has its own level and keeps the real weather.
 *
 * The target is an anonymous class, the most fragile kind of name, so these
 * are require = 0: should a future version rename it, the attributes just
 * follow the real weather again instead of crashing on world load.
 */
@Mixin(targets = "net.minecraft.world.attribute.WeatherAttributes$WeatherAccess$1")
public class WeatherAttributesMixin {
    @WrapOperation(method = "rainLevel", require = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getRainLevel(F)F"))
    private float hideRainLevel(Level level, float partialTicks, Operation<Float> original) {
        if (level.isClientSide() && ClearSightConfig.get().hideWeather) {
            return 0.0F;
        }
        return original.call(level, partialTicks);
    }

    @WrapOperation(method = "thunderLevel", require = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getThunderLevel(F)F"))
    private float hideThunderLevel(Level level, float partialTicks, Operation<Float> original) {
        if (level.isClientSide() && ClearSightConfig.get().hideWeather) {
            return 0.0F;
        }
        return original.call(level, partialTicks);
    }
}
