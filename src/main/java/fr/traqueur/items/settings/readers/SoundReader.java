package fr.traqueur.items.settings.readers;

import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.readers.Reader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;

public class SoundReader implements Reader<Sound> {
    @Override
    public Sound read(String value) throws StructuraException {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(NamespacedKey.fromString(value.toLowerCase()));
    }
}
