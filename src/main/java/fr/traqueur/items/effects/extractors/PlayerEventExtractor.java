package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Generic extractor for all PlayerEvent subclasses.
 * <p>
 * This extractor serves as a fallback for any PlayerEvent that doesn't have
 * a specific extractor registered. It simply extracts the item in the player's
 * main hand.
 * <p>
 * Specific extractors (like {@link PlayerInteractExtractor}) can override this
 * behavior for events that have more specific item sources.
 */
public class PlayerEventExtractor implements ItemSourceExtractor<PlayerEvent> {

    @Override
    public ExtractionResult extract(PlayerEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        return new ExtractionResult(player, item);
    }
}