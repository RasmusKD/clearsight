package com.rasmus.clearsight;

import com.mojang.blaze3d.platform.InputConstants;
import com.rasmus.clearsight.config.ClearSightConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ClearSightClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("clearsight", "category"));

    private record Toggle(KeyMapping key, String label, Runnable flip, BooleanSupplier read) {
    }

    private final List<Toggle> toggles = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        // All unbound by default: bind the ones you actually use in Controls.
        register("darkness", "Darkness effect",
                () -> ClearSightConfig.get().disableDarknessEffect ^= true,
                () -> ClearSightConfig.get().disableDarknessEffect);
        register("blindness", "Blindness effect",
                () -> ClearSightConfig.get().disableBlindnessEffect ^= true,
                () -> ClearSightConfig.get().disableBlindnessEffect);
        register("waterfog", "Water fog",
                () -> ClearSightConfig.get().clearWaterFog ^= true,
                () -> ClearSightConfig.get().clearWaterFog);
        register("lavafog", "Lava fog",
                () -> ClearSightConfig.get().clearLavaFog ^= true,
                () -> ClearSightConfig.get().clearLavaFog);
        register("powdersnowfog", "Powder snow fog",
                () -> ClearSightConfig.get().clearPowderSnowFog ^= true,
                () -> ClearSightConfig.get().clearPowderSnowFog);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Toggle toggle : toggles) {
                while (toggle.key().consumeClick()) {
                    toggle.flip().run();
                    AutoConfig.getConfigHolder(ClearSightConfig.class).save();
                    if (client.player != null) {
                        client.player.sendSystemMessage(Component.literal(
                                toggle.label() + ": "
                                        + (toggle.read().getAsBoolean() ? "removed" : "vanilla")));
                    }
                }
            }
        });
    }

    private void register(String id, String label, Runnable flip, BooleanSupplier read) {
        KeyMapping key = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.clearsight." + id, InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(), CATEGORY));
        toggles.add(new Toggle(key, label, flip, read));
    }
}
