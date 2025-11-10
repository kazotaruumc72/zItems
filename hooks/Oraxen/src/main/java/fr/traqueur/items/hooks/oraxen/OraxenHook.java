package fr.traqueur.items.hooks.oraxen;

import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.blocks.CustomBlockProvider;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.CustomBlockProviderRegistry;
import fr.traqueur.items.api.registries.Registry;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import io.th0rgal.oraxen.mechanics.provided.gameplay.block.BlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.furniture.FurnitureMechanic;
import io.th0rgal.oraxen.utils.drops.Loot;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

@AutoHook("Oraxen")
public class OraxenHook implements Hook {

    @Override
    public void onEnable() {
        Registry.get(CustomBlockProviderRegistry.class).register("oraxen", new OraxenProvider());
    }

    private record OraxenProvider() implements CustomBlockProvider {

        @Override
        public Optional<List<ItemStack>> getCustomBlockDrop(Block block, Player player) {
            FurnitureMechanic mechanic = OraxenFurniture.getFurnitureMechanic(block);
            BlockMechanic blockMechanic = OraxenBlocks.getBlockMechanic(block);

            if(mechanic == null && blockMechanic == null) {
                return  Optional.empty();
            }

            if (mechanic != null) {
                return Optional.of(mechanic.getDrop().getLootToDrop(player).stream().map(Loot::getItemStack).toList());
            }

            return Optional.of(blockMechanic.getDrop().getLootToDrop(player).stream().map(Loot::getItemStack).toList());
        }

        @Override
        public Optional<String> getCustomBlockId(Block block) {
            BlockMechanic blockMechanic = OraxenBlocks.getBlockMechanic(block);

            if(blockMechanic == null) {
                return  Optional.empty();
            }

            return Optional.of(blockMechanic.getItemID());
        }

        @Override
        public void placeCustomBlock(String itemId, Block block) {
            BlockData data = OraxenBlocks.getOraxenBlockData(itemId);
            if (data != null) {
                block.setBlockData(data);
            }
            throw new IllegalArgumentException("Oraxen block with item ID " + itemId + " does not exist.");
        }

    }
}
