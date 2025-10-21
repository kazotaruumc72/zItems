package fr.traqueur.items.utils;

import fr.traqueur.items.PlatformType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating and modifying ItemStacks with Paper/Spigot compatibility.
 * Works with Adventure Components throughout the code and handles conversion only at the final step.
 */
public class ItemUtil {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

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

        if (PlatformType.isPaper()) {
            // Use Paper's native Adventure API
            meta.displayName(displayName);
        } else {
            // Convert Component to legacy format for Spigot
            String legacy = LEGACY_SERIALIZER.serialize(displayName);
            meta.setDisplayName(legacy);
        }

        itemStack.setItemMeta(meta);
    }

    /**
     * Sets the lore of an ItemStack using a list of Components.
     * Handles Paper (native) vs Spigot (legacy conversion) automatically.
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

        if (PlatformType.isPaper()) {
            // Use Paper's native Adventure API
            meta.lore(lore);
        } else {
            // Convert Components to legacy format for Spigot
            List<String> legacyLore = new ArrayList<>();
            for (Component line : lore) {
                String legacy = LEGACY_SERIALIZER.serialize(line);
                legacyLore.add(legacy);
            }
            meta.setLore(legacyLore);
        }

        itemStack.setItemMeta(meta);
    }

    /**
     * Adds a Component line to the lore of an ItemStack.
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

        if (PlatformType.isPaper()) {
            List<Component> lore = meta.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.add(line);
            meta.lore(lore);
        } else {
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            String legacy = LEGACY_SERIALIZER.serialize(line);
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
    public static ItemStack createItem(Material material, int amount, Component displayName, List<Component> lore) {
        ItemStack itemStack = ItemStack.of(material, amount);

        if (displayName != null) {
            setDisplayName(itemStack, displayName);
        }

        if (lore != null && !lore.isEmpty()) {
            setLore(itemStack, lore);
        }

        return itemStack;
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

    /**
     * Creates a new ItemStack with the specified material and display name.
     *
     * @param material    The material of the item
     * @param displayName The display name as a Component
     * @return The created ItemStack
     */
    public static ItemStack createItem(Material material, Component displayName) {
        return createItem(material, 1, displayName, null);
    }

    /**
     * Creates a new ItemStack with the specified material, display name, and lore.
     *
     * @param material    The material of the item
     * @param displayName The display name as a Component
     * @param lore        The lore lines as Components
     * @return The created ItemStack
     */
    public static ItemStack createItem(Material material, Component displayName, List<Component> lore) {
        return createItem(material, 1, displayName, lore);
    }
}