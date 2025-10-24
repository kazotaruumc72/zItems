package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Rail;

/**
 * BlockData metadata for rail blocks.
 * Sets the rail shape.
 */
public record RailMeta(Rail.Shape shape) implements BlockDataMeta<Rail> {

    @Override
    public void apply(Rail blockData) {
        blockData.setShape(shape);
    }
}
