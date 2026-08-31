package com.rasmus.clearsight.config;

import com.rasmus.clearsight.ClearSightGate;
import java.util.Collections;
import java.util.List;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

/**
 * Toggles that hand you information the game hides are shown, but cannot be
 * clicked unless you are in your own world or hold operator rights. Cloth
 * re-checks the requirement while the screen is open, so the toggle greys out
 * the moment you are somewhere it would not apply, and comes back by itself.
 *
 * Shown-but-inactive beats the alternatives: hiding it would take your saved
 * setting out of sight, and disabling it permanently would be wrong on the
 * title screen, where there is no world to judge yet.
 */
public final class ClearSightGuiRegistry {

    private ClearSightGuiRegistry() {
    }

    public static void register() {
        GuiRegistry registry = AutoConfigClient.getGuiRegistry(ClearSightConfig.class);
        registry.registerPredicateProvider(
                ClearSightGuiRegistry::gatedToggle,
                field -> "clearLavaFog".equals(field.getName()));
    }

    private static List<AbstractConfigListEntry> gatedToggle(
            String i18n, java.lang.reflect.Field field, Object config, Object defaults,
            me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess access) {
        field.setAccessible(true);
        boolean value;
        boolean defaultValue;
        try {
            value = field.getBoolean(config);
            defaultValue = field.getBoolean(defaults);
        } catch (IllegalAccessException e) {
            // Fall back to the ordinary toggle rather than dropping the option.
            return access.get(i18n, field, config, defaults, access);
        }
        return Collections.singletonList(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.translatable(i18n), value)
                .setDefaultValue(defaultValue)
                .setTooltip(Component.translatable(i18n + ".@Tooltip"))
                .setSaveConsumer(newValue -> {
                    try {
                        field.setBoolean(config, newValue);
                    } catch (IllegalAccessException ignored) {
                    }
                })
                .setRequirement(ClearSightGate::allowed)
                .build());
    }
}
