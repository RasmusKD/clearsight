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

    // The level != null guard is the last line of defense for a stale gate
    // (a disconnect mid-transition never reaches onClose): singleplayer
    // world loads show this screen before any level exists, and nothing may
    // ever be hidden while there is provably nothing to show behind it.
    // The time budget brings the vanilla screen back when a load stalls
    // server-side, so the stall is visible instead of an invisible screen
    // holding dead keys with no explanation.
    private static boolean clearsight$hidden() {
        return ClearSightConfig.get().hideWorldLoadScreen && LoadingGate.hideLevelLoad
                && net.minecraft.client.Minecraft.getInstance().level != null
                && net.minecraft.util.Util.getMillis() - LoadingGate.raisedAt < LoadingGate.HIDE_BUDGET_MS;
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
