package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The rocket flame trail sits directly in your face while boosting an
 * elytra. The explosion burst is untouched; only the trail goes.
 */
@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void skipTrailParticles(Level level, ParticleOptions particle,
            double x, double y, double z, double dx, double dy, double dz) {
        if (!ClearSightConfig.get().hideFireworkTrail || !level.isClientSide()) {
            level.addParticle(particle, x, y, z, dx, dy, dz);
        }
    }
}
