package fr.traqueur.items.utils;

import fr.maxlego08.menu.api.dupe.DupeManager;
import fr.traqueur.items.PlatformType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating and modifying ItemStacks with Paper/Spigot compatibility.
 * Works with Adventure Components throughout the code and handles conversion only at the final step.
 */
public class ItemUtil {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final NamespacedKey DUPE_KEY = new NamespacedKey(Bukkit.getServer().getPluginManager().getPlugin("zMenu"), DupeManager.KEY);

    public static ItemStack cloneItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        ItemStack clone = itemStack.clone();

        clone.editPersistentDataContainer(container -> {
            if (container.has(DUPE_KEY)) {
                container.remove(DUPE_KEY);
            }
        });

        return clone;
    }

    /**
     * Sets the display name of an ItemStack using a Component.
     * Handles Paper (native) vs Spigot (legacy conversion) automatically.
     *
     * @param itemStack   The ItemStack to modify
     * @param displayName The display name as a Component
     */
    public static void setDisplayName(ItemStack itemStack, Component displayName) {
        if (itemStack == null || displayName == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        Component processedDisplayName = displayName.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);


        if (PlatformType.isPaper()) {
            // Use Paper's native Adventure API
            meta.displayName(processedDisplayName);
        } else {
            // Convert Component to legacy format for Spigot
            String legacy = LEGACY_SERIALIZER.serialize(processedDisplayName);
            meta.setDisplayName(legacy);
        }

        itemStack.setItemMeta(meta);
    }

    /**
     * Sets the lore of an ItemStack using a list of Components.
     * Handles Paper (native) vs Spigot (legacy conversion) automatically.
     * Automatically disables italic decoration for all lore lines.
     *
     * @param itemStack The ItemStack to modify
     * @param lore      The lore lines as Components
     */
    public static void setLore(ItemStack itemStack, List<Component> lore) {
        if (itemStack == null || lore == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        // Disable italic decoration for all lore lines
        List<Component> processedLore = new ArrayList<>();
        for (Component line : lore) {
            processedLore.add(line.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }

        if (PlatformType.isPaper()) {
            // Use Paper's native Adventure API
            meta.lore(processedLore);
        } else {
            // Convert Components to legacy format for Spigot
            List<String> legacyLore = new ArrayList<>();
            for (Component line : processedLore) {
                String legacy = LEGACY_SERIALIZER.serialize(line);
                legacyLore.add(legacy);
            }
            meta.setLore(legacyLore);
        }

        itemStack.setItemMeta(meta);
    }

    /**
     * Adds a Component line to the lore of an ItemStack.
     * Automatically disables italic decoration for the added line.
     *
     * @param itemStack The ItemStack to modify
     * @param line      The line to add as a Component
     */
    public static void addLoreLine(ItemStack itemStack, Component line) {
        if (itemStack == null || line == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        // Disable italic decoration for the line
        Component processedLine = line.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        if (PlatformType.isPaper()) {
            List<Component> lore = meta.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.add(processedLine);
            meta.lore(lore);
        } else {
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            String legacy = LEGACY_SERIALIZER.serialize(processedLine);
            lore.add(legacy);
            meta.setLore(lore);
        }

        itemStack.setItemMeta(meta);
    }

    /**
     * Gets the display name of an ItemStack as a Component.
     * Works on both Paper and Spigot.
     *
     * @param itemStack The ItemStack to get the display name from
     * @return The display name as a Component, or null if none
     */
    public static Component getDisplayName(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }

        if (PlatformType.isPaper()) {
            // Use Paper's native Adventure API
            return meta.displayName();
        } else {
            // Convert from legacy format for Spigot
            String legacy = meta.getDisplayName();
            if (legacy == null) {
                return null;
            }
            return LEGACY_SERIALIZER.deserialize(legacy);
        }
    }

    /**
     * Gets the lore of an ItemStack as a List of Components.
     * Works on both Paper and Spigot.
     *
     * @param itemStack The ItemStack to get the lore from
     * @return The lore as a list of Components, or null if none
     */
    public static List<Component> getLore(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }

        if (PlatformType.isPaper()) {
            // Use Paper's native Adventure API
            return meta.lore();
        } else {
            // Convert from legacy format for Spigot
            List<String> lore = meta.getLore();
            if (lore == null) {
                return null;
            }
            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(LEGACY_SERIALIZER.deserialize(line));
            }
            return componentLore;
        }
    }

    /**
     * Creates a new ItemStack with the specified material, amount, display name, and lore.
     *
     * @param material    The material of the item
     * @param amount      The number of items in the stack
     * @param displayName The display name as a Component
     * @param lore        The lore lines as Components
     * @return The created ItemStack
     */
    public static ItemStack createItem(Material material, int amount, Component displayName, List<Component> lore, Component itemName) {
        ItemStack itemStack = ItemStack.of(material, amount);

        if (displayName != null) {
            setDisplayName(itemStack, displayName);
        }

        if (lore != null && !lore.isEmpty()) {
            setLore(itemStack, lore);
        }

        if (itemName != null) {
            setItemName(itemStack, itemName);
        }

        return itemStack;
    }

    private static void setItemName(ItemStack itemStack, Component itemName) {
        if (itemStack == null || itemName == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        Component processedItemName = itemName.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);


        if (PlatformType.isPaper()) {
            // Use Paper's native Adventure API
            meta.itemName(processedItemName);
        } else {
            // Convert Component to legacy format for Spigot
            String legacy = LEGACY_SERIALIZER.serialize(processedItemName);
            meta.setItemName(legacy);
        }

        itemStack.setItemMeta(meta);
    }

    public static void applyDamageToItem(ItemStack item, int damage, Player player) {
        if (item == null || item.getType().isAir()) {
            return;
        }

        PlayerItemDamageEvent damageEvent = new PlayerItemDamageEvent(player, item, damage, damage);
        damageEvent.callEvent();
        if (damageEvent.isCancelled()) {
            return;
        }

        if (item.getItemMeta() instanceof Damageable damageable) {
            int currentDamage = damageable.getDamage();
            int maxDurability = item.getType().getMaxDurability();

            int newDamage = currentDamage + damageEvent.getDamage();

            if (newDamage >= maxDurability) {
                // Item breaks
                player.getInventory().setItemInMainHand(null);
            } else {
                damageable.setDamage(newDamage);
                item.setItemMeta(damageable);
            }
        }
    }
}