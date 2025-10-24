package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Bisected;

/**
 * BlockData metadata for bisected blocks (doors, plants, etc.).
 * Sets which half of the block this is.
 */
public record BisectedMeta(Bisected.Half half) implements BlockDataMeta<Bisected> {

    @Override
    public void apply(Bisected blockData) {
        blockData.setHalf(half);
    }
}
