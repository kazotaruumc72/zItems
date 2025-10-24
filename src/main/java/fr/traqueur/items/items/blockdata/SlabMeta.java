package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Slab;

/**
 * BlockData metadata for slab blocks.
 * Sets the slab type (top, bottom, double).
 */
@AutoBlockDataMeta("slab")
public record SlabMeta(Slab.Type type) implements BlockDataMeta<Slab> {

    @Override
    public void apply(Slab blockData) {
        blockData.setType(type);
    }
}
