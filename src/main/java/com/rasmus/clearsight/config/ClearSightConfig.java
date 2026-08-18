package com.rasmus.clearsight.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = "clearsight")
public class ClearSightConfig implements ConfigData {

    @ConfigEntry.Category("effects")
    @ConfigEntry.Gui.Tooltip
    public boolean disableDarknessEffect = true;

    @ConfigEntry.Category("effects")
    @ConfigEntry.Gui.Tooltip
    public boolean disableBlindnessEffect = false;

    @ConfigEntry.Category("effects")
    @ConfigEntry.Gui.Tooltip
    public boolean disableNauseaEffect = false;

    @ConfigEntry.Category("effects")
    @ConfigEntry.Gui.Tooltip
    public boolean hideEffectParticles = true;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearWaterFog = false;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearLavaFog = false;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearPowderSnowFog = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideFireOverlay = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideTotemAnimation = true;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideHurtCam = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideMaskOverlay = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideFrostOverlay = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hidePortalOverlay = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideFireworkTrail = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideWeather = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideDebugSightLines = false;

    public static void register() {
        AutoConfig.register(ClearSightConfig.class, GsonConfigSerializer::new);
    }

    public static ClearSightConfig get() {
        return AutoConfig.getConfigHolder(ClearSightConfig.class).getConfig();
    }
}
