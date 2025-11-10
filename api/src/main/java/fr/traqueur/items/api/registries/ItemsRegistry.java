package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.items.Item;

/**
 * Registry for managing Item instances.
 */
public abstract class ItemsRegistry extends FileBasedRegistry<String, Item> {

    /**
     * Constructs an ItemsRegistry with the given plugin.
     *
     * @param plugin    The ItemsPlugin instance
     * @param directory The directory where item data files are stored.
     */
    protected ItemsRegistry(ItemsPlugin plugin, String directory) {
        super(plugin, directory, "Items Registry");
    }
}