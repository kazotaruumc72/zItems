package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;

/**
 * Registry for managing Effect instances.
 */
public abstract class EffectsRegistry extends FileBasedRegistry<String, Effect> {

    /**
     * Constructs an EffectsRegistry with the specified ItemsPlugin.
     *
     * @param directory the directory where effect data is stored
     * @param plugin the ItemsPlugin instance
     */
    protected EffectsRegistry(ItemsPlugin plugin, String directory) {
        super(plugin, directory, "Effects Registry");
    }
}
