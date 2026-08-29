package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.AmbientParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Biome ambient particles: white ash in basalt deltas, ash in soul sand
 * valleys, crimson and warped spores. Redirecting the spawn roll leaves the
 * rest of doAnimateTick (block animate ticks, drips, marker particles)
 * untouched.
 */
@Mixin(ClientLevel.class)
public class AmbientParticleMixin {

    @Redirect(method = "doAnimateTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/attribute/AmbientParticle;canSpawn(Lnet/minecraft/util/RandomSource;)Z"))
    private boolean skipBiomeParticles(AmbientParticle particle, RandomSource random) {
        return !ClearSightConfig.get().hideBiomeParticles && particle.canSpawn(random);
    }
}
