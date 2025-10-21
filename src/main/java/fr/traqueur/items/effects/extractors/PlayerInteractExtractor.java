package fr.traqueur.items.effects.extractors;

import fr.traqueur.items.api.effects.ExtractorMeta;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Extractor for PlayerInteractEvent.
 * <p>
 * Prioritizes the item from the event itself, which represents the item
 * the player clicked with. Falls back to the item in main hand if the
 * event doesn't specify an item.
 * <p>
 * This is used for effects that trigger on interaction, such as
 * Infinite Bucket, Sell Stick, or Farming Hoe interactions.
 */
@ExtractorMeta(PlayerInteractEvent.class)
public class PlayerInteractExtractor implements ItemSourceExtractor<PlayerInteractEvent> {

    @Override
    public ExtractionResult extract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot slot = event.getHand();
        ItemStack item = slot == EquipmentSlot.HAND 
                ? player.getInventory().getItemInMainHand() 
                : player.getInventory().getItemInOffHand();

        return new ExtractionResult(player, item);
    }
}