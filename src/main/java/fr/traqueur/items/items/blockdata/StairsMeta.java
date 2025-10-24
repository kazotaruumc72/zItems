package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Stairs;

/**
 * BlockData metadata for stairs blocks.
 * Sets the stairs shape.
 */
@AutoBlockDataMeta("stairs")
public record StairsMeta(Stairs.Shape shape) implements BlockDataMeta<Stairs> {

    @Override
    public void apply(Stairs blockData) {
        blockData.setShape(shape);
    }
}
