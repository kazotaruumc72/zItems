package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.Lightable;

/**
 * BlockData metadata for lightable blocks (torches, campfires, etc.).
 * Sets whether the block is lit.
 */
@AutoBlockDataMeta("lightable")
public record LightableMeta(boolean lit) implements BlockDataMeta<Lightable> {

    @Override
    public void apply(Lightable blockData) {
        blockData.setLit(lit);
    }
}
