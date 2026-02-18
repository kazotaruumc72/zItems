package fr.traqueur.items.hooks.mythicmobs;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * Listener that intercepts spawner spawn events and replaces vanilla mobs
 * with MythicMobs mobs when the spawner has a mythic-mob-type configured in its PDC.
 */
public class MythicMobsSpawnerListener implements Listener {

    private final NamespacedKey mythicMobTypeKey;

    public MythicMobsSpawnerListener(JavaPlugin plugin) {
        this.mythicMobTypeKey = new NamespacedKey(plugin, "mythic_mob_type");
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        CreatureSpawner spawner = event.getSpawner();
        String mythicType = spawner.getPersistentDataContainer().get(mythicMobTypeKey, PersistentDataType.STRING);

        if (mythicType == null || mythicType.isEmpty()) {
            return;
        }

        Optional<MythicMob> mythicMob = MythicBukkit.inst().getMobManager().getMythicMob(mythicType);
        if (mythicMob.isEmpty()) {
            return;
        }

        event.setCancelled(true);
        mythicMob.get().spawn(BukkitAdapter.adapt(event.getLocation()), 1);
    }
}
