package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.items.Item;

import java.nio.file.Path;

public interface ItemsRegistry extends Registry<String, Item> {
    void loadFromFolder(Path items);
}