package fr.traqueur.items.hooks.nexo;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.blocks.CustomBlockProvider;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.CustomBlockProviderRegistry;
import fr.traqueur.items.api.registries.Registry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

@AutoHook("Nexo")
public class NexoHook implements Hook {
    @Override
    public void onEnable() {
        Registry.get(CustomBlockProviderRegistry.class).register("nexo", new NexoProvider());
    }

    private record NexoProvider() implements CustomBlockProvider {

        @Override
        public Optional<List<ItemStack>> getCustomBlockDrop(Block block, Player player) {
            boolean mechanic = NexoFurniture.isFurniture(block.getLocation());
            boolean isBlockMechanic = NexoBlocks.isCustomBlock(block);

            if(!mechanic && !isBlockMechanic) {
                return Optional.empty();
            }

            if (mechanic) {
                FurnitureMechanic furnitureMechanic = NexoFurniture.furnitureMechanic(block.getLocation());
                if (furnitureMechanic != null) {
                    ItemBuilder itemBuilder = NexoItems.itemFromId(furnitureMechanic.getItemID());
                    if (itemBuilder != null) {
                        return Optional.of(List.of(itemBuilder.build()));
                    }
                }
            }

            CustomBlockMechanic blockMechanic = NexoBlocks.customBlockMechanic(block);
            if (blockMechanic != null) {
                ItemBuilder itemBuilder = NexoItems.itemFromId(blockMechanic.getItemID());
                if (itemBuilder != null) {
                    return Optional.of(List.of(itemBuilder.build()));
                }
            }

            return Optional.empty();
        }

        @Override
        public ItemStack getItem(Player __, String itemId) {
            ItemBuilder itemBuilder = NexoItems.itemFromId(itemId);
            if (itemBuilder != null) {
                return itemBuilder.build();
            }
            return null;
        }
    }
}
