package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;

public abstract class EffectsRegistry extends FileBasedRegistry<String, Effect> {
    protected EffectsRegistry(ItemsPlugin plugin) {
        super(plugin, "effects", "Effects Registry");
    }
}
