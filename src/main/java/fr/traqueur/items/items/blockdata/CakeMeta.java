package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.Cake;

/**
 * BlockData metadata for cake blocks.
 * Sets how many bites have been taken.
 */
@AutoBlockDataMeta("cake")
public record CakeMeta(int bites) implements BlockDataMeta<Cake> {

    @Override
    public void apply(Cake blockData) {
        if (bites >= 0 && bites <= blockData.getMaximumBites()) {
            blockData.setBites(bites);
        }
    }
}
