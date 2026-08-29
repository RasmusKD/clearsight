package com.rasmus.clearsight;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

/**
 * Shared identifier sniffing for the camera texture overlays. Everything the
 * HUD draws over the camera routes through one method with the texture id:
 * powder snow frost, the nether portal swirl, and equippable masks like the
 * carved pumpkin. Which is which is spelled out in the path.
 */
public final class OverlayGate {

    private OverlayGate() {
    }

    /**
     * The effective fire overlay height: the slider, or 0 while Fire
     * Resistance runs and the flames are pure noise, if that toggle is on.
     */
    public static int fireOverlayHeight() {
        ClearSightConfig config = ClearSightConfig.get();
        if (config.hideFireWhenResistant) {
            Player player = Minecraft.getInstance().player;
            if (player != null && player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                return 0;
            }
        }
        return config.fireOverlayHeight;
    }

    public static boolean shouldHide(Identifier texture) {
        ClearSightConfig config = ClearSightConfig.get();
        String path = texture.getPath();
        if (path.contains("powder_snow")) {
            return config.hideFrostOverlay;
        }
        if (path.contains("portal")) {
            return config.hidePortalOverlay;
        }
        return config.hideMaskOverlay;
    }
}
