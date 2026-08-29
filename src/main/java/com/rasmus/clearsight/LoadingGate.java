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
 */
public final class LoadingGate {

    /** A load stuck past this stays hidden forever without it; showing the
     *  vanilla screen again makes a server-side stall visible instead of
     *  leaving the player in a world with dead keys and no explanation. */
    public static final long HIDE_BUDGET_MS = 10_000L;

    public static volatile boolean hideLevelLoad = false;
    public static volatile boolean worldReady = false;
    public static volatile long raisedAt = 0L;

    private LoadingGate() {
    }
}
