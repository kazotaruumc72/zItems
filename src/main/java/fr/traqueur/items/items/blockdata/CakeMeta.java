package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Cake;

/**
 * BlockData metadata for cake blocks.
 * Sets how many bites have been taken.
 */
public record CakeMeta(int bites) implements BlockDataMeta<Cake> {

    @Override
    public void apply(Cake blockData) {
        if (bites >= 0 && bites <= blockData.getMaximumBites()) {
            blockData.setBites(bites);
        }
    }
}
