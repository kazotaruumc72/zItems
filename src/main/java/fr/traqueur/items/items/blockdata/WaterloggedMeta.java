package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Waterlogged;

/**
 * BlockData metadata for waterlogged blocks.
 * Sets whether the block is waterlogged.
 */
@BlockDataMetaMeta("waterlogged")
public record WaterloggedMeta(boolean waterlogged) implements BlockDataMeta<Waterlogged> {

    @Override
    public void apply(Waterlogged blockData) {
        blockData.setWaterlogged(waterlogged);
    }
}
