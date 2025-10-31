package fr.traqueur.items.hooks.superiorskyblock2;

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
        var island = SuperiorSkyblockAPI.getIslandAt(location);
        return island == null || island.hasPermission(superiorPlayer, IslandPrivilege.getByName("BREAK"));
    }
}
