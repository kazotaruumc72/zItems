package fr.traqueur.items.settings.readers;

import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.readers.Reader;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectTypeReader implements Reader<PotionEffectType> {
    @Override
    public PotionEffectType read(String s) throws StructuraException {
        PotionEffectType type = PotionEffectType.getByName(s.toUpperCase());
        if (type == null) {
            throw new StructuraException("Unknown potion effect type: " + s);
        }
        return type;
    }
}