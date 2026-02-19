package fr.traqueur.items.hooks.mythicmobs;

import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.hooks.Hook;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hook for MythicMobs integration.
 * When enabled, registers a spawner listener that replaces vanilla spawns
 * with MythicMobs mobs when a spawner has a mythic-mob-type configured.
 */
@AutoHook("MythicMobs")
public class MythicMobsHook implements Hook {

    private final JavaPlugin plugin;

    public MythicMobsHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(new MythicMobsSpawnerListener(plugin), plugin);
    }
}
