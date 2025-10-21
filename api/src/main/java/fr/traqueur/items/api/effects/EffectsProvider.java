package fr.traqueur.items.api.effects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Functional interface for retrieving Effects from an ItemStack's PersistentDataContainer.
 * <p>
 * This abstraction allows the effects system to be decoupled from the specific
 * PDC implementation details. Implementations should read the PDC and deserialize
 * the list of Effects attached to the item.
 * <p>
 * Example implementation:
 * <pre>{@code
 * public List<Effect> getEffects(ItemStack item) {
 *     if (item == null || !item.hasItemMeta()) {
 *         return List.of();
 *     }
 *
 *     PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
 *     return pdc.get(EFFECTS_KEY, new EffectsDataType());
 * }
 * }</pre>
 */
@FunctionalInterface
public interface EffectsProvider {

    /**
     * Retrieves all Effects attached to the given ItemStack.
     * <p>
     * This method should read the item's PersistentDataContainer and return
     * a list of all Effects that should be triggered when this item is used.
     *
     * @param itemStack the item to check for effects
     * @return a list of Effects (empty if none found or item is invalid)
     */
    List<Effect> getEffects(ItemStack itemStack);
}