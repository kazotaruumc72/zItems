package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for BlockPlaceEvent.
 * <p>
 * Extracts the item that was used to place the block.
 * This is useful for effects that should trigger when placing blocks,
 * such as special placement bonuses or attribute applicators.
 */
public class BlockPlaceExtractor implements ItemSourceExtractor<BlockPlaceEvent> {

    @Override
    public ExtractionResult extract(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        // Get the item used to place the block
        ItemStack item = event.getItemInHand();

        return new ExtractionResult(player, item);
    }
}