package fr.traqueur.items.items.listeners;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.managers.ItemsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * Listener that handles grindstone usage restrictions for custom items.
 *
 * <p>This listener prevents custom items from being used in a grindstone unless:
 * <ul>
 *     <li>Both items in the grindstone are custom items</li>
 *     <li>Both items have the same ID</li>
 *     <li>Both items have grindstoneEnabled set to true</li>
 * </ul>
 *
 * <p>If only one item is a custom item, or if the conditions above are not met,
 * the grindstone operation is cancelled.
 */
public class GrindstoneListener implements Listener {

    /**
     * Handles grindstone events to restrict usage for custom items.
     *
     * <p>The grindstone operation is cancelled if:
     * <ul>
     *     <li>Only one of the two items is a custom item</li>
     *     <li>Both items are custom but have different IDs</li>
     *     <li>Either custom item has grindstoneEnabled set to false</li>
     * </ul>
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onGrindstone(PrepareGrindstoneEvent event) {
        ItemStack upperItem = event.getInventory().getUpperItem();
        ItemStack lowerItem = event.getInventory().getLowerItem();

        ItemsPlugin plugin = JavaPlugin.getPlugin(ItemsPlugin.class);
        ItemsManager itemsManager = plugin.getManager(ItemsManager.class);
        if (itemsManager == null) {
            return;
        }

        Optional<Item> optionalUpperItem = itemsManager.getCustomItem(upperItem);
        Optional<Item> optionalLowerItem = itemsManager.getCustomItem(lowerItem);

        // If neither item is custom, allow the operation
        if (optionalUpperItem.isEmpty() && optionalLowerItem.isEmpty()) {
            return;
        }

        // If only one item is custom, cancel the operation
        if (optionalUpperItem.isEmpty() || optionalLowerItem.isEmpty()) {
            event.setResult(null);
            return;
        }

        // Both items are custom - check if they're compatible
        Item upperCustomItem = optionalUpperItem.get();
        Item lowerCustomItem = optionalLowerItem.get();

        // Cancel if items have different IDs or if grindstone is not enabled for either
        if (!upperCustomItem.id().equals(lowerCustomItem.id())
                || !upperCustomItem.settings().grindstoneEnabled()
                || !lowerCustomItem.settings().grindstoneEnabled()) {
            event.setResult(null);
        }
    }
}
