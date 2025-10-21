package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for EntityDeathEvent.
 * <p>
 * Extracts the weapon from the player who killed the entity.
 * Returns null if the entity was not killed by a player.
 * <p>
 * This is used for effects that trigger on mob kills, such as
 * Auto Sell for mob drops or XP Boost effects.
 */
public class EntityDeathExtractor implements ItemSourceExtractor<EntityDeathEvent> {

    @Override
    public ExtractionResult extract(EntityDeathEvent event) {
        // Check if the entity was killed by a player
        if (!(event.getEntity().getKiller() instanceof Player killer)) {
            return null;
        }

        // Extract the weapon used to kill
        ItemStack weapon = killer.getInventory().getItemInMainHand();

        return new ExtractionResult(killer, weapon);
    }
}