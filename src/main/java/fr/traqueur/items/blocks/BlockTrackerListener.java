package fr.traqueur.items.blocks;

import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.managers.ItemsManager;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Optional;

/**
 * Listener that handles block tracking for custom items.
 * <p>
 * - Tracks blocks when placed from custom items
 * - Drops correct custom item when tracked blocks are broken
 * - Manages chunk load/unload for persistence
 */
public class BlockTrackerListener implements Listener {

    private final BlockTracker tracker;
    private final ItemsManager itemsManager;

    public BlockTrackerListener(BlockTracker tracker, ItemsManager manager) {
        this.tracker = tracker;
        this.itemsManager = manager;
    }

    /**
     * Tracks blocks placed from custom items.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();

        // Check if the placed block came from a custom item
        Optional<Item> customItem = itemsManager.getCustomItem(itemInHand);
        if (customItem.isPresent()) {
            Block placedBlock = event.getBlockPlaced();
            tracker.trackBlock(placedBlock, customItem.get().id());
        }
    }

    /**
     * Handles block break events for tracked blocks.
     * Drops the correct custom item instead of the default block drops.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Optional<String> trackedItemId = tracker.getTrackedItemId(block);

        if (trackedItemId.isPresent()) {
            Player player = event.getPlayer();
            ItemsRegistry itemsRegistry = Registry.get(ItemsRegistry.class);
            Item customItem = itemsRegistry.getById(trackedItemId.get());

            if (customItem != null) {
                // Cancel default drops
                event.setDropItems(false);

                // Drop the custom item instead
                ItemStack customItemStack = customItem.build(player, 1);
                // Drop custom item at block location
                block.getWorld().dropItemNaturally(block.getLocation(), customItemStack);

                // Untrack the block
                tracker.untrackBlock(block);
            }
        }
    }

    /**
     * Loads tracked blocks into cache when a chunk loads.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        tracker.loadChunk(event.getChunk());
    }

    /**
     * Saves tracked blocks from cache when a chunk unloads.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkUnload(ChunkUnloadEvent event) {
        tracker.unloadChunk(event.getChunk());
    }
}