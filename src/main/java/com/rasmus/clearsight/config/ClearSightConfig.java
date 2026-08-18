package com.rasmus.clearsight.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
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
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int fireOverlayHeight = 100;

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
    public boolean hideMenuDarkening = false;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuInventory = true;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuStorage = true;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuWorkstations = true;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuVillager = true;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuSignsBooks = true;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuPause = true;

    @ConfigEntry.Category("menus")
    @ConfigEntry.Gui.Tooltip
    public boolean menuOther = true;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideFireworkTrail = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideWeather = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean showWeatherWhileFishing = true;

    public boolean hideFallingWeather() {
        return hideWeather && !fishingException();
    }

    public boolean hideAllWeather() {
        return hideWeather && !fishingException();
    }

    /**
     * Rain speeds up fishing, so while the bobber is out the weather is
     * information the player asked for. The exception suspends every
     * weather removal until the line is reeled in.
     */
    private boolean fishingException() {
        if (!showWeatherWhileFishing) {
            return false;
        }
        var player = Minecraft.getInstance().player;
        return player != null && player.fishing != null;
    }

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideDebugSightLines = false;

    @ConfigEntry.Category("world")
    @ConfigEntry.Gui.Tooltip
    public boolean hideBossDarkening = false;

    public static void register() {
        AutoConfig.register(ClearSightConfig.class, GsonConfigSerializer::new);
    }

    public static ClearSightConfig get() {
        return AutoConfig.getConfigHolder(ClearSightConfig.class).getConfig();
    }
}
