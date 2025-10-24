package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.items.Item;

public abstract class ItemsRegistry extends FileBasedRegistry<String, Item> {

    protected ItemsRegistry(ItemsPlugin plugin) {
        super(plugin, "items", "Items Registry");
    }
}