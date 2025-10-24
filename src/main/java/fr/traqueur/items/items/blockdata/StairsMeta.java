package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Stairs;

/**
 * BlockData metadata for stairs blocks.
 * Sets the stairs shape.
 */
public record StairsMeta(Stairs.Shape shape) implements BlockDataMeta<Stairs> {

    @Override
    public void apply(Stairs blockData) {
        blockData.setShape(shape);
    }
}
