package fr.traqueur.items.settings.readers;

import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.readers.Reader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.PatternType;

public class PatternTypeReader implements Reader<PatternType> {
    @Override
    public PatternType read(String value) throws StructuraException {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getOrThrow(NamespacedKey.minecraft(value.toLowerCase()));
    }
}
