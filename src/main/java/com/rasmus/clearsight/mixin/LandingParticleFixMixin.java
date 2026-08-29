package com.rasmus.clearsight.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * TEST FIX for MC-1691: landing and sprint particles sample the block 0.2
 * below the feet (getOnPosLegacy), so anything thinner than 0.2 (trapdoors,
 * carpet, lily pads, snow layers) shows the particles of the block below.
 * The game already solves both halves of this elsewhere: the supporting
 * block tracks what the entity actually stands on, and the step sound
 * system prefers the block at the feet for perception overlays like snow
 * layers and carpets. Reusing both makes particles pick the same block a
 * step sound would.
 */
@Mixin(Entity.class)
public abstract class LandingParticleFixMixin {

    @Shadow
    protected abstract BlockPos getPrimaryStepSoundBlockPos(BlockPos pos);

    @Redirect(method = {"doCheckFallDamage", "spawnSprintParticle"},
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getOnPosLegacy()Lnet/minecraft/core/BlockPos;"))
    private BlockPos useSupportingBlock(Entity entity) {
        BlockPos base = entity.mainSupportingBlockPos.orElseGet(entity::getOnPosLegacy);
        return this.getPrimaryStepSoundBlockPos(base);
    }
}
