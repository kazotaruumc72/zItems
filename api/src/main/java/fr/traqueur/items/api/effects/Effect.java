package fr.traqueur.items.api.effects;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an effect that can be applied to an item.
 */
public interface Effect {

    /**
     * Gets the representation of this effect.
     *
     * @return the effect representation
     */
    EffectRepresentation representation();

    /**
     * Gets the unique identifier of this effect.
     *
     * @return the effect ID
     */
    String id();

    /**
     * Gets the type of this effect.
     *
     * @return the effect type
     */
    String type();

    /**
     * Gets the settings associated with this effect.
     *
     * @return the effect settings
     */
    EffectSettings settings();

    /**
     * Gets the display name of this effect.
     *
     * @return the effect display name
     */
    Component displayName();

}
