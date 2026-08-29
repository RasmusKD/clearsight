package com.rasmus.clearsight.mixin;

import com.rasmus.clearsight.FrozenFrame;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * clearClientLevel HEAD is the one moment that is both late enough (the
 * main target still holds the last presented world frame, the fog state
 * is still valid) and early enough (the reconfiguration screen is set,
 * and one frame force-rendered, a few lines later).
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "clearClientLevel", at = @At("HEAD"))
    private void captureFrozenFrame(Screen screen, CallbackInfo ci) {
        if (ClearSightConfig.get().seamlessServerSwitch
                && ((Minecraft) (Object) this).level != null) {
            FrozenFrame.capture();
        }
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("HEAD"))
    private void releaseFrozenFrame(Screen screen, boolean transferring, boolean keepResourcePacks,
            CallbackInfo ci) {
        FrozenFrame.release();
    }
}
