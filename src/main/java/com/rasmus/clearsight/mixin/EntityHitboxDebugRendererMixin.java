package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * F3+B draws view and motion arrows out of every entity on top of the
 * hitboxes. When hidden, the arrows collapse to zero length while the
 * boxes stay: hitboxes without the spaghetti.
 */
@Mixin(EntityHitboxDebugRenderer.class)
public class EntityHitboxDebugRendererMixin {

    @Redirect(method = "showHitboxes", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/gizmos/Gizmos;arrow(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;I)Lnet/minecraft/gizmos/GizmoProperties;"))
    private GizmoProperties collapseArrows(Vec3 from, Vec3 to, int color) {
        if (ClearSightConfig.get().hideDebugSightLines) {
            return Gizmos.arrow(from, from, color);
        }
        return Gizmos.arrow(from, to, color);
    }
}
