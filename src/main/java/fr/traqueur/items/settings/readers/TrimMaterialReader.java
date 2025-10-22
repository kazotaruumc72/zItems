package fr.traqueur.items.settings.readers;

import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.readers.Reader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.trim.TrimMaterial;

import java.util.NoSuchElementException;

public class TrimMaterialReader implements Reader<TrimMaterial> {
    @Override
    public TrimMaterial read(String s) throws StructuraException {
        try {
            return RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.TRIM_MATERIAL)
                    .getOrThrow(NamespacedKey.minecraft(s.toLowerCase()));
        } catch (NoSuchElementException e) {
            throw new StructuraException("Unknown trim material: " + s);
        }
    }
}