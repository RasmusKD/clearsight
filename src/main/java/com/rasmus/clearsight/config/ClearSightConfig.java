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

    @ConfigEntry.Category("effects")
    @ConfigEntry.Gui.Tooltip
    public boolean hideElderGuardianJumpscare = false;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearWaterFog = false;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearLavaFog = false;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearPowderSnowFog = false;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearNetherFog = false;

    @ConfigEntry.Category("fog")
    @ConfigEntry.Gui.Tooltip
    public boolean clearDistanceFog = false;

    @ConfigEntry.Category("loading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean instantResourceReload = false;

    @ConfigEntry.Category("loading")
    @ConfigEntry.Gui.Tooltip
    public boolean hideTitleFade = false;

    @ConfigEntry.Category("loading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hideWorldLoadScreen = false;

    @ConfigEntry.Category("loading")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean seamlessServerSwitch = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int fireOverlayHeight = 100;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideFireWhenResistant = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int shieldHeight = 100;

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

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideSpyglassOverlay = false;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean hideSleepOverlay = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuInventory = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuStorage = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuWorkstations = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuVillager = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuSignsBooks = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuPause = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuOther = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideFireworkTrail = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideWeather = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideDebugSightLines = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideBossDarkening = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideBiomeParticles = false;

    public static void register() {
        AutoConfig.register(ClearSightConfig.class, GsonConfigSerializer::new);
    }

    public static ClearSightConfig get() {
        return AutoConfig.getConfigHolder(ClearSightConfig.class).getConfig();
    }
}
