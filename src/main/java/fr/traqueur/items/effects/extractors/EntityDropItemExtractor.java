package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for BlockBreakEvent.
 * <p>
 * Extracts the item in the player's main hand when they break a block.
 * This is typically used for tools like pickaxes with effects like
 * Vein Mining, Hammer, or Auto Sell.
 */
public class EntityDropItemExtractor implements ItemSourceExtractor<EntityDropItemEvent> {

    @Override
    public ExtractionResult extract(EntityDropItemEvent event) {
        EntityDamageEvent lastDamage = event.getEntity().getLastDamageCause();
        if(lastDamage instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
            if(entityDamageByEntityEvent.getDamager() instanceof Player player) {
                ItemStack item = player.getInventory().getItemInMainHand();
                return new ExtractionResult(player, item);
            }
        }
        return null;
    }
}