package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for PlayerItemDamageEvent.
 * <p>
 * Extracts the item that is being damaged.
 * This is useful for effects like "Unbreakable" that can cancel
 * or modify durability damage.
 */
public class PlayerItemDamageExtractor implements ItemSourceExtractor<PlayerItemDamageEvent> {

    @Override
    public ExtractionResult extract(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        return new ExtractionResult(player, item);
    }
}