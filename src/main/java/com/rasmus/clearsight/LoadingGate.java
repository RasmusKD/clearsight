package com.rasmus.clearsight;

/**
 * Whether the current level loading screen covers a respawn or dimension
 * change (world behind it, safe to hide) rather than a fresh join (nothing
 * behind it yet). Two flags, because "a respawn packet arrived" is not
 * enough: lobby servers force-respawn players during the initial join,
 * while the join screen is still up over a chunkless world. worldReady
 * records that the previous load actually finished, so only transitions
 * away from a world the player has seen ever hide the screen. Both reset
 * in handleLogin, so no stale value survives a reconnect.
 *
 * The stall clock is progress-based: as long as the screen's smoothed
 * progress keeps rising, a slow load stays hidden however long it takes;
 * only progress standing still past the budget brings the vanilla screen
 * back, so a genuine server-side stall is visible instead of an invisible
 * screen holding dead keys with no explanation.
 */
public final class LoadingGate {

    public static final long STALL_BUDGET_MS = 10_000L;

    /**
     * Written by ClearSightMixinPlugin at mixin-application time (this
     * class has no Minecraft imports, so loading it that early is safe):
     * false when the frozen-frame mixins were gated off (rival mod or
     * unknown MC version), so the capture hook in the always-applied
     * packet mixin does not burn textures nothing will draw or release.
     */
    public static volatile boolean frozenFrameActive = false;

    public static volatile boolean hideLevelLoad = false;
    public static volatile boolean worldReady = false;
    public static volatile float lastProgress = -1.0F;
    public static volatile long lastProgressAt = 0L;

    private LoadingGate() {
    }
}
