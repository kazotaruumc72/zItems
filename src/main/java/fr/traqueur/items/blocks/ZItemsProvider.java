package fr.traqueur.items.blocks;

import fr.traqueur.items.api.blocks.CustomBlockProvider;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class ZItemsProvider implements CustomBlockProvider {
    @Override
    public Optional<List<ItemStack>> getCustomBlockDrop(Block block, Player player) {
        Optional<ItemStack> customDrop = BlockTracker.get().getCustomBlockDrop(block, player);
        if (customDrop.isPresent()) {
            BlockTracker.get().untrackBlock(block);
        }
        return customDrop.map(List::of);
    }

    @Override
    public ItemStack getItem(Player player, String itemId) {
        Item item = Registry.get(ItemsRegistry.class).getById(itemId);
        if (item != null) {
            return item.build(player, 1);
        }
        return null;
    }
}
