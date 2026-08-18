package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The green totem burst, but only on the entity the camera sits in: your
 * own burst fills the screen, everyone else's is information.
 */
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Redirect(method = "handleEntityEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/particle/ParticleEngine;createTrackingEmitter(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/particles/ParticleOptions;I)V"))
    private void skipOwnTotemBurst(ParticleEngine engine, Entity entity,
            ParticleOptions particle, int count) {
        if (particle == ParticleTypes.TOTEM_OF_UNDYING
                && ClearSightConfig.get().hideTotemAnimation
                && entity == Minecraft.getInstance().getCameraEntity()) {
            return;
        }
        engine.createTrackingEmitter(entity, particle, count);
    }
}
