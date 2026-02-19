package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.blocks.CustomBlockProvider;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Registry for managing custom block providers from various sources.
 *
 * <p>This registry allows multiple custom block systems to coexist:
 * <ul>
 *   <li>zItems internal tracking system</li>
 *   <li>ItemsAdder custom blocks</li>
 *   <li>Nexo custom blocks</li>
 *   <li>Oraxen custom blocks</li>
 *   <li>Any other custom block plugin</li>
 * </ul>
 *
 * <p>When querying for a custom block drop, the registry iterates through
 * all registered providers until one returns a non-empty result.
 *
 * <p>Usage in effect handlers:
 * <pre>{@code
 * var registry = Registry.get(CustomBlockProviderRegistry.class);
 * var customDrop = registry.getCustomBlockDrop(block, player);
 *
 * if (customDrop.isPresent()) {
 *     context.addDrop(customDrop.get());
 * } else {
 *     // Use vanilla drops
 *     context.addDrops(block.getDrops(context.itemSource()));
 * }
 * }</pre>
 */
public interface CustomBlockProviderRegistry extends Registry<String, CustomBlockProvider> {

    /**
     * Attempts to get a custom ItemStack drop for a block by querying all
     * registered providers.
     *
     * <p>Iterates through providers in registration order and returns the
     * first non-empty result. If no provider recognizes the block as custom,
     * returns {@link Optional#empty()}.
     *
     * <p><b>Important:</b> This method should only be called when the block
     * is confirmed to be broken (after event validation), as providers may
     * perform irreversible operations like untracking.
     *
     * @param block the block being broken
     * @param player the player breaking the block (may be null)
     * @return Optional containing the custom ItemStack, or empty if not a custom block
     */
    Optional<List<ItemStack>> getCustomBlockDrop(Block block, Player player);
}
