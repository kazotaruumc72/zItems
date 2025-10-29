package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.items.settings.models.ItemStackWrapper;
import fr.traqueur.structura.annotations.Options;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * BlockState configuration for container blocks (chests, barrels, shulker boxes, etc.).
 * Allows filling container inventory with items.
 */
@AutoBlockStateMeta("container")
public record ContainerStateMeta(
        @Options(optional = true) Map<Integer, ItemStackWrapper> items
) implements BlockStateMeta<Container> {

    @Override
    public void apply(Player player, Container container) {
        if (items == null || items.isEmpty()) {
            return;
        }

        var inventory = container.getInventory();
        items.forEach((slot, itemSettings) -> {
            if (slot >= 0 && slot < inventory.getSize()) {
                inventory.setItem(slot, itemSettings.build(player));
            }
        });
    }
}