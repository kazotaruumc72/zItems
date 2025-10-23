package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.items.Item;

import java.nio.file.Path;

public abstract class ItemsRegistry extends FileBasedRegistry<String, Item> {

    protected ItemsRegistry(ItemsPlugin plugin, String[] exampleFiles) {
        super(plugin, exampleFiles, "items", "Items Registry");
    }
}