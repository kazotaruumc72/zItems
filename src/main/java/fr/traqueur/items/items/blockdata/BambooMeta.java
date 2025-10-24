package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Bamboo;

/**
 * BlockData metadata for bamboo blocks.
 * Sets the bamboo leaves type.
 */
public record BambooMeta(Bamboo.Leaves leaves) implements BlockDataMeta<Bamboo> {

    @Override
    public void apply(Bamboo blockData) {
        blockData.setLeaves(leaves);
    }
}
