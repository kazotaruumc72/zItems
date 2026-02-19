package fr.traqueur.items.api.blocks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a functional interface to check if a player has access to a specific location.
 */
@FunctionalInterface
public interface LocationAccess {

    /**
     * Checks if the given player has access to the specified location.
     *
     * @param player   the player whose access is being checked
     * @param location the location to check access for
     * @return true if the player has access to the location, false otherwise
     */
    boolean hasAccess(Player player, Location location);

}
