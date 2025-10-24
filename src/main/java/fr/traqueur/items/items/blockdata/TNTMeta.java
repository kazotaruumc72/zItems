package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.TNT;

/**
 * BlockData metadata for TNT blocks.
 * Sets whether the TNT is unstable.
 */
@AutoBlockDataMeta("tnt")
public record TNTMeta(boolean unstable) implements BlockDataMeta<TNT> {

    @Override
    public void apply(TNT blockData) {
        blockData.setUnstable(unstable);
    }
}
