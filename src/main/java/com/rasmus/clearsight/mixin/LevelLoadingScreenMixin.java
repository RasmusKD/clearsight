package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.LoadingGate;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hide, never close: only the two extract methods (pure rendering plus the
 * progress narration cadence) are cancelled, so the screen's tick, the
 * load tracker's ready conditions, and the close path with its key-state
 * resync all stay vanilla. The lesson is borrowed from upstream mods that
 * tried closing early and collected the fall-through-world and stuck-screen
 * bugs that came with it.
 */
@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {

    @org.spongepowered.asm.mixin.Shadow
    private float smoothedProgress;

    // The level != null guard is the last line of defense for a stale gate
    // (a disconnect mid-transition never reaches onClose): singleplayer
    // world loads show this screen before any level exists, and nothing may
    // ever be hidden while there is provably nothing to show behind it.
    // The stall budget is progress-based: rising smoothedProgress keeps
    // resetting the clock, so only a load whose progress stands still past
    // the budget gets the vanilla screen back.
    private boolean clearsight$hidden() {
        if (!ClearSightConfig.get().hideWorldLoadScreen || !LoadingGate.hideLevelLoad
                || net.minecraft.client.Minecraft.getInstance().level == null) {
            return false;
        }
        long now = net.minecraft.util.Util.getMillis();
        // Movement in EITHER direction counts as activity: vanilla reuses
        // the screen instance for back-to-back transitions, so a new load
        // starts with smoothedProgress still high from the previous one
        // and decays before it climbs. Only a value standing still means
        // a stall.
        if (Math.abs(this.smoothedProgress - LoadingGate.lastProgress) > 0.001F) {
            LoadingGate.lastProgress = this.smoothedProgress;
            LoadingGate.lastProgressAt = now;
        }
        return now - LoadingGate.lastProgressAt < LoadingGate.STALL_BUDGET_MS;
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void hideForeground(GuiGraphicsExtractor extractor, int mouseX, int mouseY,
            float partialTick, CallbackInfo ci) {
        if (clearsight$hidden()) {
            ci.cancel();
        }
    }

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void hideBackground(GuiGraphicsExtractor extractor, int mouseX, int mouseY,
            float partialTick, CallbackInfo ci) {
        if (clearsight$hidden()) {
            ci.cancel();
        }
    }

    // onClose only fires when the load tracker reported the level ready, so
    // this is the one honest signal that a world is now on screen.
    @Inject(method = "onClose", at = @At("HEAD"))
    private void clearGate(CallbackInfo ci) {
        LoadingGate.hideLevelLoad = false;
        LoadingGate.worldReady = true;
    }
}
