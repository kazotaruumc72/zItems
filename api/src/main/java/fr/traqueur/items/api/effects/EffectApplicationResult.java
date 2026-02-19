package fr.traqueur.items.api.effects;

/**
 * Represents the result of applying an effect to an item.
 */
public enum EffectApplicationResult {
    /**
     * The effect was successfully applied to the item.
     */
    SUCCESS,

    /**
     * The effect is already present on the item.
     */
    ALREADY_PRESENT,

    /**
     * The effect is incompatible with existing effects on the item.
     */
    INCOMPATIBLE,

    /**
     * Additional effects are not allowed on this item.
     */
    NOT_ALLOWED,

    /**
     * This specific effect is disabled for this item.
     */
    DISABLED,

    /**
     * The effect handler was not found.
     */
    HANDLER_NOT_FOUND
}