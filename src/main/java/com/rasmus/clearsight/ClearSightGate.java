package com.rasmus.clearsight;

import net.minecraft.client.Minecraft;
import net.minecraft.server.permissions.Permissions;

/**
 * Some toggles hand you information the game deliberately hides from you.
 * Seeing through lava is the clear case: it turns an opaque hazard into a
 * window onto ores, mobs and players. Removing an overlay that only sits on
 * your own screen is not the same thing, so only the see-through toggles ask
 * this.
 *
 * The rule is the one Asteroid uses for the same class of feature: your own
 * world, or a server that already trusts you with operator rights.
 */
public final class ClearSightGate {

    private ClearSightGate() {
    }

    public static boolean allowed() {
        Minecraft client = Minecraft.getInstance();
        if (client.hasSingleplayerServer()) {
            return true;
        }
        // GAMEMASTERS is vanilla permission level 2, the same bar the game
        // uses for command blocks and /gamemode.
        return client.player != null
                && client.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
    }
}
