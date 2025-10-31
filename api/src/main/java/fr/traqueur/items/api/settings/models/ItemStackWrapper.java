package fr.traqueur.items.api.settings.models;

import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import fr.traqueur.structura.api.Loadable;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Lightweight configuration for creating ItemStacks.
 * This is different from ItemSettings which is for complete custom items.
 *
 * <p>This allows two modes:
 * <ul>
 *   <li>Simple mode: Create a basic ItemStack with material, amount, and optional display properties</li>
 *   <li>Reference mode: Reference a custom zItem by its ID</li>
 * </ul>
 *
 * <p>Example YAML (simple mode):
 * <pre>
 * material: DIAMOND
 * amount: 64
 * display-name: "&lt;red&gt;Special Diamond"
 * </pre>
 *
 * <p>Example YAML (reference mode):
 * <pre>
 * item-id: "my_custom_item"
 * amount: 16
 * </pre>
 */
public record ItemStackWrapper(
        @Options(optional = true) Material material,

        @Options(optional = true) @DefaultInt(1) int amount,

        @Options(optional = true) String itemId,

        @Options(optional = true) Component displayName,

        @Options(optional = true) List<Component> lore
) implements Loadable {

    public ItemStackWrapper {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be at least 1");
        }
        if(itemId == null && material == null) {
            throw new IllegalArgumentException("Either 'item-id' or 'material' must be specified");
        }
    }

    /**
     * Builds an ItemStack from these settings.
     *
     * @param player the player for context (used when building custom items)
     * @return the created ItemStack
     * @throws IllegalStateException if neither itemId nor material is specified
     */
    public @NotNull ItemStack build(@Nullable Player player) {
        // Reference mode: build from custom item ID
        if (itemId != null && !itemId.isEmpty()) {
            ItemsRegistry registry = Registry.get(ItemsRegistry.class);
            if (registry != null) {
                Item item = registry.getById(itemId);
                if (item != null) {
                    return item.build(player, amount);
                }
            }
            throw new IllegalStateException("Custom item with ID '" + itemId + "' not found in registry");
        }

        ItemStack itemStack = new ItemStack(material, amount);

        // Apply display name and lore if present
        if (displayName != null || (lore != null && !lore.isEmpty())) {
            itemStack.editMeta(meta -> {
                if (displayName != null) {
                    meta.displayName(displayName);
                }
                if (lore != null && !lore.isEmpty()) {
                    meta.lore(lore);
                }
            });
        }

        return itemStack;
    }
}