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

    /**
     * The loading hooks are cosmetic, so their correct failure mode on an
     * unknown future MC version is silently off, not a launch crash; within
     * the supported window they still hard-fail via defaultRequire.
     */
    private static boolean knownLoadingVersion() {
        String version = FabricLoader.getInstance().getModContainer("minecraft")
                .map(c -> c.getMetadata().getVersion().getFriendlyString()).orElse("");
        return version.startsWith("26.1") || version.startsWith("26.2");
    }

    /** kennytv's mod changed id over the years; accept both. */
    private static boolean forceCloseLoaded() {
        return FabricLoader.getInstance().isModLoaded("forcecloseworldloadingscreen")
                || FabricLoader.getInstance().isModLoaded("forcecloseloadingscreen");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Rival gating is per hook, matched to what each rival actually
        // touches. rrls owns the reload overlay's fade constants (it
        // ModifyConstants the same inlined literals at priority 999, which
        // would consume them before our require=1 injectors and crash) but
        // never touches the level loading screen, so only the overlay hook
        // yields to it. kennytv's mod covers both screens. The title hook
        // stays on always: both rivals leave the real TitleScreen in a
        // state our scaling cannot corrupt.
        if (mixinClassName.endsWith("LoadingOverlayMixin")) {
            return knownLoadingVersion() && !forceCloseLoaded()
                    && !FabricLoader.getInstance().isModLoaded("rrls");
        }
        if (mixinClassName.endsWith("LevelLoadingScreenMixin")) {
            return knownLoadingVersion() && !forceCloseLoaded();
        }
        // kennytv's mod replaces the reconfiguration screen with its own
        // bridge and does its own frame capturing; ours would be inert
        // beside it but still burn a texture per transfer. The flag also
        // reaches the capture hook living in the always-applied packet
        // mixin, which this gate cannot switch off.
        if (mixinClassName.endsWith("MinecraftMixin")
                || mixinClassName.endsWith("ReconfigFreezeMixin")) {
            boolean active = knownLoadingVersion() && !forceCloseLoaded();
            com.rasmus.clearsight.LoadingGate.frozenFrameActive = active;
            return active;
        }
        if (mixinClassName.endsWith("TitleScreenMixin")) {
            return knownLoadingVersion();
        }
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
        if (mixinClassName.endsWith("FireOverlayLegacyMixin")) {
            return isLegacyGui();
        }
        if (mixinClassName.endsWith("FireOverlayModernMixin")) {
            return !isLegacyGui();
        }
        if (mixinClassName.endsWith("ShieldLegacyMixin")) {
            return isLegacyGui();
        }
        if (mixinClassName.endsWith("ShieldModernMixin")) {
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
