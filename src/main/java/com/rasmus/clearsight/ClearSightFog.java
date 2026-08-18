package com.rasmus.clearsight;

import net.minecraft.client.renderer.fog.FogData;

public final class ClearSightFog {
    private static final float FAR_AWAY = 1_000_000.0F;

    private ClearSightFog() {
    }

    /**
     * Pushes every fog plane out of sight. The color is left alone: it is
     * still needed as the fog color source, and it tints nothing when the
     * fog itself starts a thousand kilometers out.
     */
    public static void clear(FogData fogData) {
        fogData.environmentalStart = FAR_AWAY;
        fogData.environmentalEnd = FAR_AWAY + 1.0F;
        fogData.renderDistanceStart = FAR_AWAY;
        fogData.renderDistanceEnd = FAR_AWAY + 1.0F;
    }
}
