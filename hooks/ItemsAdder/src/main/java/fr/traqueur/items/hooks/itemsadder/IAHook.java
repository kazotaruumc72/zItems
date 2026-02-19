package fr.traqueur.items.hooks.itemsadder;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;
import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.blocks.CustomBlockProvider;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.items.ItemProvider;
import fr.traqueur.items.api.registries.CustomBlockProviderRegistry;
import fr.traqueur.items.api.registries.ItemProviderRegistry;
import fr.traqueur.items.api.registries.Registry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@AutoHook("ItemsAdder")
public class IAHook implements Hook {
    @Override
    public void onEnable() {
        Registry.get(CustomBlockProviderRegistry.class).register("itemsadder", new IABlockProvider());
        Registry.get(ItemProviderRegistry.class).register("itemsadder", new IAItemProvider());
    }

    private record IABlockProvider() implements CustomBlockProvider {

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
        public Optional<String> getCustomBlockId(Block block) {
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
            if (customBlock != null) {
                return Optional.of(customBlock.getId());
            }
            return Optional.empty();
        }

        @Override
        public void placeCustomBlock(String itemId, Block block) {
            CustomStack customStack = CustomStack.getInstance(itemId);
            if (customStack == null) {
                throw new IllegalArgumentException("Invalid ItemsAdder item ID: " + itemId);
            }
            CustomBlock.place(itemId, block.getLocation());
        }
    }

    private record IAItemProvider() implements ItemProvider {

        @Override
        public @NotNull Optional<ItemStack> createItem(@Nullable Player player, @NotNull String itemId) {
            CustomStack customStack = CustomStack.getInstance(itemId);
            if (customStack == null) {
                return Optional.empty();
            }
            return Optional.of(customStack.getItemStack());
        }

        @Override
        public boolean hasItem(@NotNull String itemId) {
            return CustomStack.getInstance(itemId) != null;
        }
    }
}
