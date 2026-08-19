package com.rasmus.clearsight;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;

/**
 * Sorts every screen into a menu category, so the darkening can be removed
 * per kind of menu. Containers are grouped by their registered menu type id,
 * which also covers modded blocks using vanilla menu types.
 */
public final class ClearSightMenus {

    private ClearSightMenus() {
    }

    public static boolean shouldHideDarkening(Screen screen) {
        ClearSightConfig config = ClearSightConfig.get();
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            return config.menuInventory;
        }
        if (screen instanceof AbstractSignEditScreen
                || screen instanceof BookEditScreen || screen instanceof BookViewScreen) {
            return config.menuSignsBooks;
        }
        if (screen instanceof PauseScreen) {
            return config.menuPause;
        }
        if (screen instanceof AbstractContainerScreen<?> container) {
            String id = menuId(container);
            if (id.startsWith("generic") || id.equals("shulker_box")
                    || id.equals("hopper") || id.equals("horse")) {
                return config.menuStorage;
            }
            if (id.equals("merchant")) {
                return config.menuVillager;
            }
            if (id.equals("lectern")) {
                return config.menuSignsBooks;
            }
            if (id.isEmpty()) {
                return config.menuOther;
            }
            // crafting, furnaces, anvil, smithing, enchantment, brewing,
            // loom, grindstone, stonecutter, cartography, beacon, crafter
            return config.menuWorkstations;
        }
        return config.menuOther;
    }

    private static String menuId(AbstractContainerScreen<?> screen) {
        try {
            MenuType<?> type = screen.getMenu().getType();
            Identifier key = BuiltInRegistries.MENU.getKey(type);
            return key == null ? "" : key.getPath();
        } catch (UnsupportedOperationException e) {
            // the player inventory menu has no registered type
            return "";
        }
    }
}
