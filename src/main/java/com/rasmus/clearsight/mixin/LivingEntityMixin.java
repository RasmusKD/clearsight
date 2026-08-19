package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The darkness dimming is read in several places (fog, sky, light texture),
 * so instead of chasing each consumer the effect is hidden at the source:
 * every client-side query answers "no darkness". Server-side state is
 * untouched, including the singleplayer integrated server, so gameplay
 * logic never changes.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    private void hideRemovedEffects(Holder<MobEffect> effect,
            CallbackInfoReturnable<Boolean> cir) {
        if (clearsight$hidden(effect)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getEffect", at = @At("HEAD"), cancellable = true)
    private void hideRemovedEffectInstances(Holder<MobEffect> effect,
            CallbackInfoReturnable<MobEffectInstance> cir) {
        if (clearsight$hidden(effect)) {
            cir.setReturnValue(null);
        }
    }

    /**
     * The swirly ambient particles from your own active effects, the ones
     * blocking the crosshair. Other entities keep theirs, so buffs stay
     * readable in PvP. The effect itself is untouched.
     */
    @Redirect(method = "tickEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void skipEffectParticles(Level level, ParticleOptions particle,
            double x, double y, double z, double dx, double dy, double dz) {
        if (!ClearSightConfig.get().hideEffectParticles || !level.isClientSide()
                || (Object) this != Minecraft.getInstance().player) {
            level.addParticle(particle, x, y, z, dx, dy, dz);
        }
    }

    private boolean clearsight$hidden(Holder<MobEffect> effect) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!self.level().isClientSide()) {
            return false;
        }
        ClearSightConfig config = ClearSightConfig.get();
        if (effect == MobEffects.DARKNESS) {
            return config.disableDarknessEffect;
        }
        if (effect == MobEffects.BLINDNESS) {
            return config.disableBlindnessEffect;
        }
        if (effect == MobEffects.NAUSEA) {
            return config.disableNauseaEffect;
        }
        return false;
    }
}
