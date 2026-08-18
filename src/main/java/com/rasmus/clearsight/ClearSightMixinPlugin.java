package com.rasmus.clearsight;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/**
 * 26.1 calls the HUD class Gui, 26.2 renamed it Hud. Both overlay mixins
 * ship in the jar; this plugin applies only the one whose target exists in
 * the running version, keyed off the Minecraft version string.
 */
public class ClearSightMixinPlugin implements IMixinConfigPlugin {

    private static boolean isLegacyGui() {
        String version = FabricLoader.getInstance().getModContainer("minecraft")
                .map(c -> c.getMetadata().getVersion().getFriendlyString()).orElse("");
        return version.startsWith("26.1");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("GuiOverlayMixin")) {
            return isLegacyGui();
        }
        if (mixinClassName.endsWith("HudOverlayMixin")) {
            return !isLegacyGui();
        }
        if (mixinClassName.endsWith("WeatherTickLegacyMixin")) {
            return isLegacyGui();
        }
        if (mixinClassName.endsWith("WeatherTickModernMixin")) {
            return !isLegacyGui();
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName,
            IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName,
            IMixinInfo mixinInfo) {
    }
}
