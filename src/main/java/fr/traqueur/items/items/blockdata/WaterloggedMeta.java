package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.Waterlogged;

/**
 * BlockData metadata for waterlogged blocks.
 * Sets whether the block is waterlogged.
 */
@AutoBlockDataMeta("waterlogged")
public record WaterloggedMeta(boolean waterlogged) implements BlockDataMeta<Waterlogged> {

    @Override
    public void apply(Waterlogged blockData) {
        blockData.setWaterlogged(waterlogged);
    }
}
