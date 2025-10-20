package fr.traqueur.items.effects.settings.readers;

import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.readers.Reader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

public class TagReader implements Reader<Tag<Material>> {
    @Override
    public Tag<Material> read(String s) throws StructuraException {
        Tag<Material> blockTag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft(s), Material.class);
        if (blockTag != null) {
            return blockTag;
        }
        Tag<Material> itemTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, NamespacedKey.minecraft(s), Material.class);
        if (itemTag != null) {
            return itemTag;
        }
        throw new StructuraException("Tag " + s + " not found.");
    }
}
