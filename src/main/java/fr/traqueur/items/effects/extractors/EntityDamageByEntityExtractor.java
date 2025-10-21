package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for EntityDamageByEntityEvent.
 * <p>
 * Extracts the weapon from the player who damaged the entity.
 * Returns null if the damager is not a player.
 * <p>
 * This is useful for combat effects like lifesteal, damage boosts,
 * or special on-hit effects.
 */
public class EntityDamageByEntityExtractor implements ItemSourceExtractor<EntityDamageByEntityEvent> {

    @Override
    public ExtractionResult extract(EntityDamageByEntityEvent event) {
        // Check if the damager is a player
        if (!(event.getDamager() instanceof Player player)) {
            return null;
        }

        // Extract the weapon used to damage
        ItemStack weapon = player.getInventory().getItemInMainHand();

        return new ExtractionResult(player, weapon);
    }
}