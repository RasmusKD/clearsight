package com.rasmus.clearsight;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.resources.Identifier;

/**
 * Shared identifier sniffing for the camera texture overlays. Everything the
 * HUD draws over the camera routes through one method with the texture id:
 * powder snow frost, the nether portal swirl, and equippable masks like the
 * carved pumpkin. Which is which is spelled out in the path.
 */
public final class OverlayGate {

    private OverlayGate() {
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
