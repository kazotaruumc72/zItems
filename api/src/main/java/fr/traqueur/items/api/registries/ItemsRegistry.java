package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.items.ItemFolder;

public abstract class ItemsRegistry extends FileBasedRegistry<String, Item> {

    protected ItemsRegistry(ItemsPlugin plugin) {
        super(plugin, "items", "Items Registry");
    }

    /**
     * Gets the root folder structure containing all items and sub-folders.
     *
     * @return the root item folder
     */
    public abstract ItemFolder getRootFolder();
}