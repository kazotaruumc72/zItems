package fr.traqueur.items.api.effects.access;

import org.bukkit.Location;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface LocationAccess {

    boolean hasAccess(Player player, Location location);

}
