package fr.traqueur.items.api.blocks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * Provider interface for detecting and handling custom blocks from various sources
 * (zItems internal, ItemsAdder, Nexo, Oraxen, etc.).
 *
 * <p>Implementations should:
 * <ul>
 *   <li>Check if a block is a custom block from their system</li>
 *   <li>Return the appropriate ItemStack drop for custom blocks</li>
 *   <li>Handle any internal cleanup (like untracking) automatically</li>
 * </ul>
 *
 * <p>Example implementations:
 * <ul>
 *   <li>{@code InternalBlockProvider} - Uses BlockTracker for zItems custom blocks</li>
 *   <li>{@code ItemsAdderProvider} - Uses ItemsAdder API to detect custom blocks</li>
 *   <li>{@code NexoProvider} - Uses Nexo API to detect custom blocks</li>
 *   <li>{@code OraxenProvider} - Uses Oraxen API to detect custom blocks</li>
 * </ul>
 */
@FunctionalInterface
public interface CustomBlockProvider {

    /**
     * Attempts to get the custom ItemStack drop for a block.
     *
     * <p>This method should:
     * <ol>
     *   <li>Check if the block is a custom block from this provider's system</li>
     *   <li>If yes, return the appropriate custom ItemStack</li>
     *   <li>If no, return {@link Optional#empty()}</li>
     *   <li>Handle any internal cleanup (e.g., untracking) automatically</li>
     * </ol>
     *
     * <p><b>Note:</b> This method should be called ONLY when the block is confirmed
     * to be broken (after BlockBreakEvent validation). The provider may perform
     * irreversible operations like untracking.
     *
     * @param block the block being broken
     * @param player the player breaking the block (may be null in some contexts)
     * @return Optional containing the custom ItemStack, or empty if not a custom block
     */
    Optional<ItemStack> getCustomBlockDrop(Block block, Player player);
}
