package fr.traqueur.items.api.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Functional interface responsible for extracting the ItemStack source from an event.
 * This allows the system to identify which item triggered the effect.
 * <p>
 * Extractors can be registered for specific event types or generic event hierarchies.
 * For example, a {@code PlayerEventExtractor} can handle all {@code PlayerEvent} subclasses,
 * while a {@code PlayerInteractExtractor} provides specific logic for {@code PlayerInteractEvent}.
 *
 * @param <E> the event type this extractor handles
 */
@FunctionalInterface
public interface ItemSourceExtractor<E extends Event> {

    /**
     * Extracts the ItemStack source and player from the given event.
     *
     * @param event the event to extract from
     * @return an ExtractionResult containing the player and item, or null if extraction failed
     */
    @Nullable
    ExtractionResult extract(E event);

    /**
     * Represents the result of an extraction: the player who triggered the event
     * and the ItemStack that should be checked for effects.
     *
     * @param player the player who triggered the event
     * @param itemSource the ItemStack that may contain effects
     */
    record ExtractionResult(Player player, ItemStack itemSource) {

        /**
         * Checks if the extraction was successful and the result is valid.
         *
         * @return true if both player and itemSource are non-null and the item is not air
         */
        public boolean isValid() {
            return player != null
                && itemSource != null
                && !itemSource.getType().isAir();
        }
    }
}