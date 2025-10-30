package fr.traqueur.items.hooks.oraxen;

import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.CustomBlockProviderRegistry;
import fr.traqueur.items.api.registries.Registry;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.mechanics.provided.gameplay.block.BlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.furniture.FurnitureMechanic;
import io.th0rgal.oraxen.utils.drops.Loot;

import java.util.Optional;

@AutoHook("Oraxen")
public class OraxenHook implements Hook {

    @Override
    public void onEnable() {
        Registry.get(CustomBlockProviderRegistry.class).register("oraxen", (block, player) -> {
            FurnitureMechanic mechanic = OraxenFurniture.getFurnitureMechanic(block);
            BlockMechanic blockMechanic = OraxenBlocks.getBlockMechanic(block);

            if(mechanic == null && blockMechanic == null) {
                return  Optional.empty();
            }

            if (mechanic != null) {
                return Optional.of(mechanic.getDrop().getLootToDrop(player).stream().map(Loot::getItemStack).toList());
            }

            return Optional.of(blockMechanic.getDrop().getLootToDrop(player).stream().map(Loot::getItemStack).toList());

        });
    }
}
