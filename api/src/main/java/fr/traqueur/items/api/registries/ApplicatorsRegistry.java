package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.effects.Applicator;
import fr.traqueur.items.api.effects.Effect;

/**
 * Registry for effect applicators.
 * Stores all available applicators for applying effects to items.
 */
public interface ApplicatorsRegistry extends Registry<String, Applicator> {

    /**
     * Gets an applicator by effect.
     *
     * @param effect the effect
     * @return the applicator, or null if not found
     */
    Applicator getByEffect(Effect effect);
}