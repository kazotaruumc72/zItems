package fr.traqueur.items.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import fr.traqueur.items.api.effects.access.LocationAccess;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SuperiorSkyBlockLocationAccess implements LocationAccess {

    @Override
    public boolean hasAccess(Player player, Location location) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if (superiorPlayer == null) {
            return false;
        }
        return SuperiorSkyblockAPI.getIslandAt(location).hasPermission(superiorPlayer, IslandPrivilege.getByName("BREAK"));
    }
}
