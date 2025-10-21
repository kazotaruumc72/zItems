package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Generic extractor for EntityEvent.
 * <p>
 * This extractor checks if the entity involved in the event is a player.
 * If so, it extracts the item in the player's main hand.
 * <p>
 * For events involving non-player entities (e.g., EntityDeathEvent where
 * a mob was killed), specific extractors should be used.
 */
public class EntityEventExtractor implements ItemSourceExtractor<EntityEvent> {

    @Override
    public ExtractionResult extract(EntityEvent event) {
        // Check if the entity is a player
        if (event.getEntity() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            return new ExtractionResult(player, item);
        }

        // Not a player entity
        return null;
    }
}