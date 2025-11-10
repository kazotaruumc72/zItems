package fr.traqueur.items.hooks.itemsadder;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;
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

@AutoHook("ItemsAdder")
public class IAHook implements Hook {
    @Override
    public void onEnable() {
        Registry.get(CustomBlockProviderRegistry.class).register("itemsadder", new IAProvider());
    }

    private record IAProvider() implements CustomBlockProvider {

        @Override
        public Optional<List<ItemStack>> getCustomBlockDrop(Block block, Player player) {
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
            CustomFurniture customFurniture = CustomFurniture.byAlreadySpawned(block);
            if (customBlock == null && customFurniture == null) {
                return Optional.empty();
            }

            if (customFurniture != null) {
                customFurniture.remove(false);
                return Optional.ofNullable(List.of(customFurniture.getItemStack()));
            }

            return Optional.ofNullable(customBlock.getLoot());
        }

        @Override
        public ItemStack getItem(Player __, String itemId) {
            CustomStack customStack = CustomStack.getInstance(itemId);
            if (customStack != null) {
                return customStack.getItemStack();
            }
            return null;
        }
    }

}
