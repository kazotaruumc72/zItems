package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.structura.annotations.Options;
import net.kyori.adventure.text.Component;
import org.bukkit.block.EnchantingTable;
import org.bukkit.entity.Player;

/**
 * BlockState configuration for enchanting table blocks.
 * Allows setting custom name.
 */
@AutoBlockStateMeta("enchanting-table")
public record EnchantingTableStateMeta(
        @Options(optional = true) Component customName
) implements BlockStateMeta<EnchantingTable> {

    @Override
    public void apply(Player player, EnchantingTable enchantingTable) {
        if (customName != null) {
            enchantingTable.customName(customName);
        }
    }
}