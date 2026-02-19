package fr.traqueur.items.api.serialization;

import fr.traqueur.items.api.blocks.TrackedBlock;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Custom {@link PersistentDataType} for serializing {@link TrackedBlock} objects to/from chunk PDC.
 *
 * <p>This class enables storing block tracking data in chunk metadata using Bukkit's
 * {@link PersistentDataContainer} system. It allows the plugin to remember which blocks
 * were placed as custom items, ensuring correct drops when broken.</p>
 *
 * <h2>Block Tracking System</h2>
 * <p>When a custom item is placed as a block, its location and ID are stored in the
 * chunk's PersistentDataContainer. This allows:</p>
 * <ul>
 *   <li>Correct custom item drops when the block is broken</li>
 *   <li>Effect execution (e.g., Hammer, VeinMiner) on custom blocks</li>
 *   <li>Detection of custom blocks from zItems vs other plugins</li>
 *   <li>Persistence across server restarts</li>
 * </ul>
 *
 * <h2>Singleton Pattern</h2>
 * <p>This class follows the singleton pattern with a static {@link #INSTANCE} field
 * initialized by the plugin implementation during startup.</p>
 *
 * <h2>Storage Format</h2>
 * <p>Tracked blocks are stored in chunk PDC as a list of {@link TrackedBlock} objects,
 * where each block contains:</p>
 * <ul>
 *   <li><b>Position:</b> Packed 32-bit integer (9-bit Y, 4-bit X/Z chunk-relative)</li>
 *   <li><b>Item ID:</b> String identifier referencing {@link fr.traqueur.items.api.registries.ItemsRegistry}</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * // Store a tracked block when placed
 * Chunk chunk = block.getChunk();
 * PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
 *
 * TrackedBlock trackedBlock = new TrackedBlock(
 *     packedPosition,  // Chunk-relative packed position
 *     "custom_ore"     // Item ID
 * );
 *
 * // Get existing tracked blocks list
 * NamespacedKey key = new NamespacedKey(plugin, "tracked_blocks");
 * List<TrackedBlock> blocks = chunkPDC.get(key,
 *     PersistentDataType.LIST.listTypeFrom(TrackedBlockDataType.INSTANCE));
 *
 * if (blocks == null) {
 *     blocks = new ArrayList<>();
 * }
 * blocks.add(trackedBlock);
 *
 * // Save back to chunk
 * chunkPDC.set(key,
 *     PersistentDataType.LIST.listTypeFrom(TrackedBlockDataType.INSTANCE),
 *     blocks);
 *
 * // Later, when a block is broken, retrieve tracking data
 * List<TrackedBlock> trackedBlocks = chunkPDC.get(key,
 *     PersistentDataType.LIST.listTypeFrom(TrackedBlockDataType.INSTANCE));
 *
 * if (trackedBlocks != null) {
 *     for (TrackedBlock tb : trackedBlocks) {
 *         if (tb.position() == packedPosition) {
 *             // This is a tracked custom block
 *             String itemId = tb.itemId();
 *             // Drop custom item instead of vanilla
 *             break;
 *         }
 *     }
 * }
 * }</pre>
 *
 * <h2>Performance Considerations</h2>
 * <ul>
 *   <li><b>Memory Cache:</b> Implementation maintains an in-memory Guava Table for fast lookups</li>
 *   <li><b>Position Packing:</b> Uses 32-bit integers instead of full coordinates</li>
 *   <li><b>Chunk-Based Storage:</b> Only loads tracking data for active chunks</li>
 *   <li><b>Lazy Persistence:</b> PDC writes occur on chunk unload</li>
 * </ul>
 *
 * <h2>Implementation Requirements</h2>
 * <p>Concrete implementations must:</p>
 * <ol>
 *   <li>Implement methods to serialize TrackedBlock to PersistentDataContainer</li>
 *   <li>Initialize the {@link #INSTANCE} field during plugin startup</li>
 *   <li>Handle position packing/unpacking correctly</li>
 * </ol>
 *
 * @see TrackedBlock
 * @see EffectDataType
 * @see org.bukkit.persistence.PersistentDataType
 */
public abstract class TrackedBlockDataType implements PersistentDataType<PersistentDataContainer, TrackedBlock> {

    /**
     * Singleton instance of the TrackedBlockDataType implementation.
     *
     * <p>This field is initialized by the plugin implementation during startup.
     * It must be accessed after the plugin has loaded.</p>
     *
     * <p><b>Warning:</b> Accessing this field before initialization will result in
     * {@code NullPointerException}. Always ensure the plugin is loaded before using.</p>
     */
    public static TrackedBlockDataType INSTANCE;

    /**
     * Protected constructor to enforce singleton pattern.
     *
     * <p>Only the plugin implementation should instantiate this class and assign
     * the instance to {@link #INSTANCE}.</p>
     */
    protected TrackedBlockDataType() {
    }

    /**
     * Returns the primitive type used for storage (PersistentDataContainer).
     *
     * <p>TrackedBlock data is stored as a nested PersistentDataContainer containing
     * the position and item ID fields.</p>
     *
     * @return {@code PersistentDataContainer.class}
     */
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    /**
     * Returns the complex type being serialized (TrackedBlock).
     *
     * @return {@code TrackedBlock.class}
     */
    @Override
    public @NotNull Class<TrackedBlock> getComplexType() {
        return TrackedBlock.class;
    }
}