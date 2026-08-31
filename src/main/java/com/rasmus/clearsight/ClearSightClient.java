package com.rasmus.clearsight;

import com.mojang.blaze3d.platform.InputConstants;
import com.rasmus.clearsight.config.ClearSightConfig;
import com.rasmus.clearsight.config.ClearSightGuiRegistry;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class ClearSightClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("clearsight", "category"));

    @Override
    public void onInitializeClient() {
        // Must run before any config screen is built.
        ClearSightGuiRegistry.register();

        // Unbound by default: bind it in Controls if you want quick access.
        KeyMapping openConfig = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.clearsight.config", InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(), CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfig.consumeClick()) {
                client.setScreenAndShow(
                        AutoConfigClient.getConfigScreen(ClearSightConfig.class, null).get());
            }
        });
    }
}
