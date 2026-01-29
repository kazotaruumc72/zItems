package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.items.ItemProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Registry for managing item providers from various sources.
 *
 * <p>This registry allows multiple custom item systems to coexist:
 * <ul>
 *   <li>zItems internal items</li>
 *   <li>ItemsAdder custom items</li>
 *   <li>Nexo custom items</li>
 *   <li>Oraxen custom items</li>
 *   <li>Any other custom item plugin</li>
 * </ul>
 *
 * <p>When creating an item, the registry can either:
 * <ul>
 *   <li>Query a specific provider by name using {@link #createItem(String, Player, String)}</li>
 *   <li>Query all providers until one returns a result using {@link #createItem(Player, String)}</li>
 * </ul>
 *
 * <p>Usage in ItemStackWrapper:
 * <pre>{@code
 * var registry = Registry.get(ItemProviderRegistry.class);
 *
 * // Create item from specific provider
 * var item = registry.createItem("itemsadder", player, "my_custom_item");
 *
 * // Or let the registry find the right provider
 * var item = registry.createItem(player, "my_custom_item");
 * }</pre>
 */
public interface ItemProviderRegistry extends Registry<String, ItemProvider> {

    /**
     * Creates an ItemStack from a specific provider.
     *
     * @param providerName the name of the provider (e.g., "zitems", "itemsadder", "nexo", "oraxen")
     * @param player the player context (may be null)
     * @param itemId the item ID to create
     * @return Optional containing the created ItemStack, or empty if provider doesn't exist or item not found
     */
    @NotNull
    Optional<ItemStack> createItem(@NotNull String providerName, @Nullable Player player, @NotNull String itemId);

    /**
     * Attempts to create an ItemStack by querying all registered providers.
     *
     * <p>Iterates through providers in registration order and returns the
     * first non-empty result. If no provider can create the item,
     * returns {@link Optional#empty()}.
     *
     * @param player the player context (may be null)
     * @param itemId the item ID to create
     * @return Optional containing the created ItemStack, or empty if no provider can create it
     */
    @NotNull
    Optional<ItemStack> createItem(@Nullable Player player, @NotNull String itemId);

    /**
     * Checks if any provider can create an item with the given ID.
     *
     * @param itemId the item ID to check
     * @return true if any provider can create the item, false otherwise
     */
    boolean hasItem(@NotNull String itemId);

    /**
     * Checks if a specific provider can create an item with the given ID.
     *
     * @param providerName the name of the provider
     * @param itemId the item ID to check
     * @return true if the provider exists and can create the item, false otherwise
     */
    boolean hasItem(@NotNull String providerName, @NotNull String itemId);
}
