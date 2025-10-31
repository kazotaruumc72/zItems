package fr.traqueur.items.buttons;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.traqueur.items.Messages;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.managers.EffectsManager;
import fr.traqueur.items.api.models.Folder;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays folders, items, and effects (with representation and folders) in one unified inventory.
 */
public class ItemsListButton extends PaginateButton {

    private static final String METADATA_KEY_FOLDER = "zitems-current-folder";
    private static final String METADATA_KEY_EFFECTS_MODE = "zitems-showing-effects";
    private static final String METADATA_KEY_EFFECTS_FOLDER = "zitems-current-effects-folder";

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
        if (player.hasMetadata(METADATA_KEY_EFFECTS_MODE)) {
            renderEffects(player, inventory);
        } else {
            renderItems(player, inventory);
        }
    }

    /* -------------------- Items rendering -------------------- */

    private void renderItems(Player player, InventoryEngine inventory) {
        List<Element<Item>> elements = getItemElements(player);

        paginate(elements, inventory, (slot, element) -> {
            if (element.isFolder()) {
                // Render folder
                Placeholders placeholders = new Placeholders();
                placeholders.register("name", element.folder().displayName());
                placeholders.register("material", element.folder().displayMaterial().name());
                placeholders.register("model-id", String.valueOf(element.folder().displayModelId()));

                inventory.addItem(slot, getItemStack().build(player, false, placeholders))
                        .setClick(event -> {
                            Folder<Effect> effectFolder = Registry.get(EffectsRegistry.class).getRootFolder();
                            if (effectFolder.name().equalsIgnoreCase(element.folder().name())) {
                                // Enter effects mode (root)
                                player.setMetadata(METADATA_KEY_EFFECTS_MODE, new FixedMetadataValue(plugin, true));
                                player.removeMetadata(METADATA_KEY_FOLDER, plugin);
                                player.removeMetadata(METADATA_KEY_EFFECTS_FOLDER, plugin);
                            } else {
                                // Enter item folder mode
                                player.setMetadata(METADATA_KEY_FOLDER,
                                        new FixedMetadataValue(plugin, element.folder()));
                                player.removeMetadata(METADATA_KEY_EFFECTS_MODE, plugin);
                            }
                            reopen(player);
                        });
            } else {
                // Render item
                try {
                    ItemStack itemStack = element.item().build(player, 1);
                    inventory.addItem(slot, itemStack).setClick(event -> {
                        ItemStack giveItem = element.item().build(player, 1);
                        var rest = player.getInventory().addItem(giveItem);
                        rest.values().forEach(dropped ->
                                player.getWorld().dropItem(player.getLocation(), dropped)
                        );
                        Messages.ITEM_RECEIVED.send(player,
                                Placeholder.component("item", element.item().representativeName()),
                                Placeholder.parsed("amount", "1"));
                    });
                } catch (Exception e) {
                    Logger.severe("Failed to build item {}", element.item().id(), e);
                }
            }
        });
    }

    /* -------------------- Effects rendering (with folders, no back) -------------------- */

    private void renderEffects(Player player, InventoryEngine inventory) {
        List<Element<Effect>> elements = getEffectElements(player);
        EffectsManager effectsManager = plugin.getManager(EffectsManager.class);

        paginate(elements, inventory, (slot, element) -> {
            if (element.isFolder()) {
                // Folder
                Folder<Effect> folder = element.folder();
                Placeholders placeholders = new Placeholders();
                placeholders.register("name", folder.displayName());
                placeholders.register("material", folder.displayMaterial().name());
                placeholders.register("model-id", String.valueOf(folder.displayModelId()));

                inventory.addItem(slot, getItemStack().build(player, false, placeholders))
                        .setClick(event -> {
                            // Navigate into subfolder
                            player.setMetadata(METADATA_KEY_EFFECTS_FOLDER, new FixedMetadataValue(plugin, folder));
                            reopen(player);
                        });
            } else {
                // Effect item with representation
                Effect effect = element.item();
                if (effect.representation() == null) return;

                ItemStack effectItem = effectsManager.createEffectItem(effect, player);
                inventory.addItem(slot, effectItem).setClick(event -> {
                    ItemStack giveItem = effectsManager.createEffectItem(effect, player);
                    var rest = player.getInventory().addItem(giveItem);
                    rest.values().forEach(dropped ->
                            player.getWorld().dropItem(player.getLocation(), dropped)
                    );
                });
            }
        });
    }

    @Override
    public int getPaginationSize(Player player) {
        if (player.hasMetadata(METADATA_KEY_EFFECTS_MODE)) {
            return getEffectElements(player).size();
        }
        return getItemElements(player).size();
    }

    @Override
    public void onInventoryClose(Player player, InventoryEngine inventory) {
        player.removeMetadata(METADATA_KEY_FOLDER, plugin);
        player.removeMetadata(METADATA_KEY_EFFECTS_MODE, plugin);
        player.removeMetadata(METADATA_KEY_EFFECTS_FOLDER, plugin);
    }

    /* -------------------- Helpers -------------------- */

    private List<Element<Item>> getItemElements(Player player) {
        List<Element<Item>> elements = new ArrayList<>();

        ItemsRegistry registry = Registry.get(ItemsRegistry.class);
        if (registry == null) return elements;

        Folder<Item> currentFolder = registry.getRootFolder();
        if (player.hasMetadata(METADATA_KEY_FOLDER)) {
            Object value = player.getMetadata(METADATA_KEY_FOLDER).getFirst().value();
            if (value instanceof Folder<?> folder) {
                try {
                    //noinspection unchecked
                    currentFolder = (Folder<Item>) folder;
                } catch (ClassCastException e) {
                    Logger.warning("Invalid folder metadata for player {}", player.getName());
                    player.removeMetadata(METADATA_KEY_FOLDER, plugin);
                }
            }
        }

        // Add special “effects” pseudo-folder in root
        if ("root".equalsIgnoreCase(currentFolder.name())) {
            Folder<Effect> effectFolder = Registry.get(EffectsRegistry.class).getRootFolder();
            elements.add(new Element<>(new Folder<>(effectFolder.name(), effectFolder.displayName(), effectFolder.displayMaterial(), effectFolder.displayModelId(), List.of(), List.of())));
        }

        // Add sub-folders
        if (currentFolder.subFolders() != null) {
            currentFolder.subFolders().forEach(folder -> elements.add(new Element<>(folder)));
        }

        // Add items
        if (currentFolder.elements() != null) {
            currentFolder.elements().forEach(item -> elements.add(new Element<>(item)));
        }

        return elements;
    }

    private List<Element<Effect>> getEffectElements(Player player) {
        List<Element<Effect>> elements = new ArrayList<>();
        EffectsRegistry effectsRegistry = Registry.get(EffectsRegistry.class);
        if (effectsRegistry == null) return elements;

        Folder<Effect> current = effectsRegistry.getRootFolder();
        if (player.hasMetadata(METADATA_KEY_EFFECTS_FOLDER)) {
            Object value = player.getMetadata(METADATA_KEY_EFFECTS_FOLDER).getFirst().value();
            if (value instanceof Folder<?> folder) {
                try {
                    //noinspection unchecked
                    current = (Folder<Effect>) folder;
                } catch (ClassCastException e) {
                    Logger.warning("Invalid folder metadata for player {}", player.getName());
                    player.removeMetadata(METADATA_KEY_EFFECTS_FOLDER, plugin);
                }
            }
        }

        // Add subfolders
        if (current.subFolders() != null) {
            current.subFolders().forEach(folder -> elements.add(new Element<>(folder)));
        }

        // Add effects with representation
        if (current.elements() != null) {
            current.elements().stream()
                    .filter(e -> e.representation() != null)
                    .forEach(effect -> elements.add(new Element<>(effect)));
        }

        return elements;
    }

    private void reopen(Player player) {
        var invManager = plugin.getInventoryManager();
        invManager.getInventory(plugin, "items_list").ifPresentOrElse(
                inv -> invManager.openInventoryWithOldInventories(player, inv, 1),
                () -> Messages.FAILED_TO_OPEN_GUI.send(player)
        );
    }

    /**
     * Represents either an item, a folder, or an effect.
     */
    public static class Element<T> {
        private final T item;
        private final Folder<T> folder;

        public Element(T item) {
            this.item = item;
            this.folder = null;
        }

        public Element(Folder<T> folder) {
            this.item = null;
            this.folder = folder;
        }

        public T item() {
            return item;
        }

        public Folder<T> folder() {
            return folder;
        }

        public boolean isFolder() {
            return folder != null;
        }

        public boolean isItem() {
            return item != null && !(item instanceof Effect);
        }

        public boolean isEffect() {
            return item instanceof Effect;
        }
    }
}
