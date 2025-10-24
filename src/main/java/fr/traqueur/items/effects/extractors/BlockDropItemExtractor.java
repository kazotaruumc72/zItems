package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.annotations.AutoExtractor;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for BlockDropItemEvent.
 * <p>
 * Extracts the item in the player's main hand when they break a block.
 * This is typically used for tools like pickaxes with effects like
 * Vein Mining, Hammer, or Auto Sell.
 */
@AutoExtractor(BlockDropItemEvent.class)
public class BlockDropItemExtractor implements ItemSourceExtractor<BlockDropItemEvent> {

    @Override
    public ExtractionResult extract(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        return new ExtractionResult(player, item);
    }
}