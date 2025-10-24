package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.Orientation;
import org.bukkit.block.data.type.Jigsaw;

/**
 * BlockData metadata for jigsaw blocks.
 * Sets the orientation.
 */
@AutoBlockDataMeta("jigsaw")
public record JigsawMeta(Orientation orientation) implements BlockDataMeta<Jigsaw> {

    @Override
    public void apply(Jigsaw blockData) {
        blockData.setOrientation(orientation);
    }
}
