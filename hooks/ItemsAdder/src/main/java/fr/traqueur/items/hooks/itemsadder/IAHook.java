package fr.traqueur.items.hooks.itemsadder;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.CustomBlockProviderRegistry;
import fr.traqueur.items.api.registries.Registry;

import java.util.List;
import java.util.Optional;

@AutoHook("ItemsAdder")
public class IAHook implements Hook {
    @Override
    public void onEnable() {
        Registry.get(CustomBlockProviderRegistry.class).register("itemsadder", (block, player) -> {
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
        });
    }
}
