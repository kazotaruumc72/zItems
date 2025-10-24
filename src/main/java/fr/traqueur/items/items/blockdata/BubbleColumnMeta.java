package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.BubbleColumn;

/**
 * BlockData metadata for bubble column blocks.
 * Sets whether the bubble column has drag.
 */
public record BubbleColumnMeta(boolean drag) implements BlockDataMeta<BubbleColumn> {

    @Override
    public void apply(BubbleColumn blockData) {
        blockData.setDrag(drag);
    }
}
