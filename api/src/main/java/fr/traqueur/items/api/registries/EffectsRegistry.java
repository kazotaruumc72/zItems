package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;

import java.nio.file.Path;

public abstract class EffectsRegistry extends FileBasedRegistry<String, Effect> {
    protected EffectsRegistry(ItemsPlugin plugin, String[] exampleFiles) {
        super(plugin, exampleFiles, "effects", "Effects Registry");
    }
}
