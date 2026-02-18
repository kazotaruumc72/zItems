package fr.maxlego08.items.hook.mythicmobs;

import fr.maxlego08.items.api.Item;
import fr.maxlego08.items.api.hook.Hook;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MythicMobsHook implements Hook {

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        CreatureSpawner spawner = event.getSpawner();
        PersistentDataContainer pdc = spawner.getPersistentDataContainer();
        String mythicMobType = pdc.get(Item.MYTHICMOB_TYPE_KEY, PersistentDataType.STRING);

        if (mythicMobType != null) {
            event.setCancelled(true);
            Location location = event.getLocation();
            MythicBukkit.inst().getMobManager().spawnMob(mythicMobType, location);
        }
    }
}
