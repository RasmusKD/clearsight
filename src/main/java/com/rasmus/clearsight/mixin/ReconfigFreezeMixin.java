package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.FrozenFrame;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Targets Screen because ServerReconfigScreen declares neither extract
 * method; the instanceof keeps this inert for every other screen. The
 * screen instance, its tick (which drives the Connection), and its
 * disconnect timer stay vanilla: only what gets drawn changes, and past
 * the foreground budget only the background does, so the vanilla text and
 * disconnect button surface over the frozen world on a stuck transfer.
 */
@Mixin(Screen.class)
public class ReconfigFreezeMixin {

    private boolean clearsight$frozen() {
        return (Object) this instanceof ServerReconfigScreen
                && ClearSightConfig.get().seamlessServerSwitch
                && FrozenFrame.drawable();
    }

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void freezeBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            float partialTick, CallbackInfo ci) {
        if (clearsight$frozen()) {
            Screen self = (Screen) (Object) this;
            graphics.fill(0, 0, self.width, self.height, FrozenFrame.backgroundColor());
            FrozenFrame.blit(graphics, self.width, self.height);
            ci.cancel();
        }
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void freezeForeground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            float partialTick, CallbackInfo ci) {
        if (clearsight$frozen() && FrozenFrame.foregroundFresh()) {
            ci.cancel();
        }
    }
}
