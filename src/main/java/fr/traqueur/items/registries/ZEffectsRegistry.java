package fr.traqueur.items.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.items.effects.ZEffect;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;

import java.nio.file.Path;

public class ZEffectsRegistry extends EffectsRegistry {

    public ZEffectsRegistry(ItemsPlugin plugin) {
        super(plugin);
    }


    @Override
    protected Effect loadFile(Path file) {
        try {
            Effect effect = Structura.load(file, ZEffect.class);
            this.register(effect.id(), effect);
            Logger.debug("Loaded effect: " + effect.id() + " from file: " + file.getFileName());
            return effect;
        } catch (StructuraException e) {
            Logger.severe("Failed to load effect from file: " + file.getFileName(), e);
            return null;
        }
    }
}
