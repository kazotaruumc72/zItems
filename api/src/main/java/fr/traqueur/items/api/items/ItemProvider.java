package fr.traqueur.items.api.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Provider interface for creating ItemStacks from various plugin sources.
 *
 * <p>This allows zItems to use items from external plugins as base items:
 * <ul>
 *   <li>zItems internal items</li>
 *   <li>ItemsAdder custom items</li>
 *   <li>Nexo custom items</li>
 *   <li>Oraxen custom items</li>
 *   <li>Any other custom item plugin</li>
 * </ul>
 *
 * <p>Implementations should:
 * <ul>
 *   <li>Check if the item ID exists in their system</li>
 *   <li>Return the appropriate ItemStack for the given ID</li>
 *   <li>Handle player context for placeholder parsing if needed</li>
 * </ul>
 *
 * <p>Example implementations:
 * <ul>
 *   <li>{@code ZItemsProvider} - Uses ItemsRegistry for zItems custom items</li>
 *   <li>{@code ItemsAdderProvider} - Uses ItemsAdder API to create items</li>
 *   <li>{@code NexoProvider} - Uses Nexo API to create items</li>
 *   <li>{@code OraxenProvider} - Uses Oraxen API to create items</li>
 * </ul>
 */
public interface ItemProvider {

    /**
     * Attempts to create an ItemStack for the given item ID.
     *
     * <p>This method should:
     * <ol>
     *   <li>Check if the item ID exists in this provider's system</li>
     *   <li>If yes, create and return the ItemStack</li>
     *   <li>If no, return {@link Optional#empty()}</li>
     * </ol>
     *
     * @param player the player context (may be null, used for placeholders)
     * @param itemId the item ID to create
     * @return Optional containing the created ItemStack, or empty if the ID doesn't exist
     */
    @NotNull
    Optional<ItemStack> createItem(@Nullable Player player, @NotNull String itemId);

    /**
     * Checks if this provider can create an item with the given ID.
     *
     * <p>This is a lightweight check that doesn't create the item.
     * Useful for validation before attempting to create items.
     *
     * @param itemId the item ID to check
     * @return true if this provider can create the item, false otherwise
     */
    boolean hasItem(@NotNull String itemId);
}
