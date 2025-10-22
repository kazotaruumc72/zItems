package fr.traqueur.items.settings.readers;

import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.readers.Reader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.NoSuchElementException;

public class TrimPatternReader implements Reader<TrimPattern> {
    @Override
    public TrimPattern read(String s) throws StructuraException {
        try {
            return RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.TRIM_PATTERN)
                    .getOrThrow(NamespacedKey.minecraft(s.toLowerCase()));
        } catch (NoSuchElementException e) {
            throw new StructuraException("Unknown trim pattern: " + s);
        }
    }
}