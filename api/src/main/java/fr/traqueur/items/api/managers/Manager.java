package fr.traqueur.items.api.managers;

import fr.traqueur.items.api.ItemsPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public sealed interface Manager permits EffectsManager, ItemsManager {

    default ItemsPlugin getPlugin() {
        return JavaPlugin.getPlugin(ItemsPlugin.class);
    }

}
