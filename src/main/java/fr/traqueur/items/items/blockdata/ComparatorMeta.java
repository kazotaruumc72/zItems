package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Comparator;

/**
 * BlockData metadata for comparator blocks.
 * Sets the comparator mode.
 */
@AutoBlockDataMeta("comparator")
public record ComparatorMeta(Comparator.Mode mode) implements BlockDataMeta<Comparator> {

    @Override
    public void apply(Comparator blockData) {
        blockData.setMode(mode);
    }
}
