package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.SeaPickle;

/**
 * BlockData metadata for sea pickle blocks.
 * Sets the number of pickles.
 */
@AutoBlockDataMeta("sea-pickle")
public record SeaPickleMeta(int pickles) implements BlockDataMeta<SeaPickle> {

    @Override
    public void apply(SeaPickle blockData) {
        int clampedPickles = Math.max(blockData.getMinimumPickles(), Math.min(pickles, blockData.getMaximumPickles()));
        blockData.setPickles(clampedPickles);
    }
}
