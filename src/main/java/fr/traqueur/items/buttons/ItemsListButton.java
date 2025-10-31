package fr.traqueur.items.buttons;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.traqueur.items.Messages;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.items.ItemFolder;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.utils.MessageUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Button that displays items and folders in a hierarchical structure.
 * - Shows folders and items from the current folder
 * - Clicking a folder navigates into it
 * - Clicking an item gives it to the player
 */
public class ItemsListButton extends PaginateButton {

    private static final String METADATA_KEY = "zitems-current-folder";
    private final ItemsPlugin plugin;

    public ItemsListButton(Plugin plugin) {
        this.plugin = (ItemsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {
        List<Element> elements = getElements(player);

        paginate(elements, inventory, (slot, element) -> {
            if (element.isFolder()) {
                // Render folder
                Placeholders placeholders = new Placeholders();
                placeholders.register("name", element.folder.displayName());
                placeholders.register("material", element.folder.displayMaterial().name());
                placeholders.register("model-id", String.valueOf(element.folder.displayModelId()));

                inventory.addItem(slot, getItemStack().build(player, false, placeholders)).setClick(event -> {
                    // Navigate into this folder
                    player.setMetadata(METADATA_KEY, new FixedMetadataValue(this.plugin, element.folder));

                    // Reopen the inventory to refresh the view
                    var inventoryManager = plugin.getInventoryManager();
                    inventoryManager.getInventory(plugin, "items_list").ifPresentOrElse(inv -> {
                        inventoryManager.openInventoryWithOldInventories(player, inv, 1);
                    }, () -> Messages.FAILED_TO_OPEN_GUI.send(player));
                });
            } else {
                // Render item
                try {
                    ItemStack itemStack = element.item.build(player, 1);

                    inventory.addItem(slot, itemStack).setClick(event -> {
                        // Give the item to the player
                        ItemStack giveItem = element.item.build(player, 1);
                        var rest = player.getInventory().addItem(giveItem);

                        // Drop excess items if inventory is full
                        rest.values().forEach(droppedItem ->
                                player.getWorld().dropItem(player.getLocation(), droppedItem)
                        );
                        Messages.ITEM_RECEIVED.send(player,
                                Placeholder.component("item", element.item.representativeName()),
                                Placeholder.parsed("amount", "1"));
                    });
                } catch (Exception e) {
                    Logger.severe("Failed to build item {}", element.item.id(), e);
                }

            }
        });
    }

    @Override
    public int getPaginationSize(Player player) {
        return getElements(player).size();
    }

    @Override
    public void onInventoryClose(Player player, InventoryEngine inventory) {
        // Clean up metadata when inventory closes
        player.removeMetadata(METADATA_KEY, this.plugin);
    }

    /**
     * Gets elements (folders and items) for the current folder.
     *
     * @param player the player viewing the inventory
     * @return list of elements to display
     */
    private List<Element> getElements(Player player) {
        ItemsRegistry registry = Registry.get(ItemsRegistry.class);
        if (registry == null) {
            return new ArrayList<>();
        }

        // Get current folder from metadata, or use root folder
        ItemFolder currentFolder = getRootFolder();
        if (player.hasMetadata(METADATA_KEY)) {
            currentFolder = (ItemFolder) player.getMetadata(METADATA_KEY).getFirst().value();
        }

        if (currentFolder == null) {
            currentFolder = registry.getRootFolder();
        }

        List<Element> elements = new ArrayList<>();

        // Add sub-folders
        if (currentFolder.subFolders() != null) {
            currentFolder.subFolders().forEach(folder ->
                elements.add(new Element(null, folder))
            );
        }

        // Add items
        if (currentFolder.items() != null) {
            currentFolder.items().forEach(item -> {
                elements.add(new Element(item, null));
            });
        }

        return elements;
    }

    private ItemFolder getRootFolder() {
        ItemsRegistry registry = Registry.get(ItemsRegistry.class);
        if (registry == null) {
            return new ItemFolder("root", "root", Material.CHEST, -1, List.of(), List.of());
        }
        return registry.getRootFolder();
    }

    /**
     * Represents an element in the GUI - either a folder or an item.
     */
    public record Element(Item item, ItemFolder folder) {
        public boolean isFolder() {
            return folder != null;
        }
    }
}