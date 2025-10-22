package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.annotations.ExtractorMeta;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for BlockBreakEvent.
 * <p>
 * Extracts the item in the player's main hand when they break a block.
 * This is typically used for tools like pickaxes with effects like
 * Vein Mining, Hammer, or Auto Sell.
 */
@ExtractorMeta(BlockBreakEvent.class)
public class BlockBreakExtractor implements ItemSourceExtractor<BlockBreakEvent> {

    @Override
    public ExtractionResult extract(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        return new ExtractionResult(player, item);
    }
}