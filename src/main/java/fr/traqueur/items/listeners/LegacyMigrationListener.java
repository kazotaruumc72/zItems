package fr.traqueur.items.listeners;

import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.serialization.Keys;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Automatically migrates legacy zItemsOld items to modern zItems format.
 * <p>
 * <b>Legacy Format (zItemsOld):</b>
 * <ul>
 *   <li><b>Runes:</b>
 *     <ul>
 *       <li>Key: new NamespacedKey("zitems", "runes")</li>
 *       <li>Type: PersistentDataType.LIST.listTypeFrom(RuneDataType)</li>
 *       <li>Value: List of Rune objects, serialized as List&lt;String&gt; (rune names)</li>
 *     </ul>
 *   </li>
 *   <li><b>Custom Item ID:</b>
 *     <ul>
 *       <li>Key: new NamespacedKey("zitems", "item-id")</li>
 *       <li>Type: PersistentDataType.STRING</li>
 *       <li>Value: Item identifier string</li>
 *     </ul>
 *   </li>
 * </ul>
 * <p>
 * <b>Modern Format (zItems):</b>
 * <ul>
 *   <li><b>Effects:</b>
 *     <ul>
 *       <li>Key: Keys.EFFECTS</li>
 *       <li>Type: PersistentDataType.LIST.listTypeFrom(EffectDataType)</li>
 *       <li>Value: List of Effect records</li>
 *     </ul>
 *   </li>
 *   <li><b>Custom Item ID:</b>
 *     <ul>
 *       <li>Key: Keys.ITEM_ID</li>
 *       <li>Type: PersistentDataType.STRING</li>
 *       <li>Value: Item identifier string</li>
 *     </ul>
 *   </li>
 * </ul>
 * <p>
 * <b>Migration Strategy:</b>
 * <ol>
 *   <li>Migrate custom item ID from "zitems:item-id" → Keys.ITEM_ID</li>
 *   <li>Read legacy "zitems:runes" key as List&lt;String&gt; (rune names)</li>
 *   <li>For each rune name, find matching effect where effect.id() == rune name</li>
 *   <li>Create Effect records and store in Keys.EFFECTS</li>
 *   <li>Remove legacy keys to prevent re-migration</li>
 * </ol>
 * <p>
 * Migration happens automatically when equipment changes (EntityEquipmentChangedEvent).
 */
public class LegacyMigrationListener implements Listener {

    private final NamespacedKey legacyRunesKey;
    private final NamespacedKey legacyItemIdKey;

    public LegacyMigrationListener() {
        // Legacy keys from zItemsOld
        this.legacyRunesKey = new NamespacedKey("zitems", "runes");
        this.legacyItemIdKey = new NamespacedKey("zitems", "item-id");
    }

    /**
     * Migrates items when entity equipment changes.
     * <p>
     * EntityEquipmentChangedEvent covers:
     * - Player login
     * - Changing held item
     * - Equipping/unequipping armor
     * - Item durability changes
     * - Items picked up from ground
     * - Dispenser equipping items
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityEquipmentChanged(EntityEquipmentChangedEvent event) {
        // Only process players (effects are player-specific)
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        event.getEquipmentChanges().forEach((key, value) -> {
            migrateItemIfNeeded(value.newItem(), player);
            migrateItemIfNeeded(value.oldItem(), player);
        });
    }

    /**
     * Checks if an item needs migration and performs it if necessary.
     *
     * @param item   the item to potentially migrate
     * @param player the player holding the item (for logging)
     */
    private void migrateItemIfNeeded(ItemStack item, Player player) {
        if(item == null) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        boolean migrated = false;

        // Migrate legacy custom item ID if present
        if (pdc.has(legacyItemIdKey, PersistentDataType.STRING)) {
            String itemId = pdc.get(legacyItemIdKey, PersistentDataType.STRING);

            if (itemId != null && !itemId.isEmpty()) {
                Logger.debug("Migrating legacy item ID '{}' for player {}", itemId, player.getName());

                // Store in modern format
                Keys.ITEM_ID.set(pdc, itemId);

                // Remove legacy key
                pdc.remove(legacyItemIdKey);

                migrated = true;
                Logger.debug("Successfully migrated custom item ID: '{}'", itemId);
            }
        }

        // Check if item has legacy rune data
        if (!pdc.has(legacyRunesKey, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING))) {
            // If we migrated the item ID, save the changes
            if (migrated) {
                item.setItemMeta(meta);
            }
            return; // No rune migration needed
        }

        Logger.info("Migrating legacy rune item for player: {}", player.getName());

        try {
            List<String> runeNames = pdc.get(
                    legacyRunesKey,
                    PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING)
            );

            if (runeNames == null || runeNames.isEmpty()) {
                Logger.debug("No rune names found in legacy data, removing key");
                pdc.remove(legacyRunesKey);
                item.setItemMeta(meta);
                return;
            }

            Logger.debug("Found {} legacy rune names: {}", runeNames.size(), runeNames);

            EffectsRegistry effectsRegistry = Registry.get(EffectsRegistry.class);
            List<Effect> migratedEffects = new ArrayList<>();

            // Convert each rune name to an effect
            for (String runeName : runeNames) {
                // Find effect by ID (effect.id() == rune.getName())
                Effect registeredEffect = effectsRegistry.getById(runeName);

                if (registeredEffect == null) {
                    Logger.warning("No effect found for rune name: '{}' (player: {})", runeName, player.getName());
                    Logger.warning("Make sure an effect with id='{}' exists in your effects configurations", runeName);
                    continue;
                }
                migratedEffects.add(registeredEffect);
                Logger.debug("Migrated rune '{}' → effect with type '{}'", runeName, registeredEffect.type());
            }

            if (migratedEffects.isEmpty()) {
                Logger.warning("No effects could be migrated for player {} - no matching effect configs found", player.getName());
            } else {
                // Store migrated effects in modern format
                Keys.EFFECTS.set(pdc, migratedEffects);
                Logger.info("Successfully migrated {} effect(s) for player {}", migratedEffects.size(), player.getName());
            }

            // Remove legacy data to prevent re-migration
            pdc.remove(legacyRunesKey);

            // Apply updated meta
            item.setItemMeta(meta);

        } catch (Exception e) {
            Logger.severe("Error migrating legacy item for player {}: {}", e, player.getName(), e.getMessage());
        }
    }
}