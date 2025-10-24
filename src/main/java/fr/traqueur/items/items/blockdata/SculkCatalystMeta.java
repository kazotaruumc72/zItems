package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.SculkCatalyst;

/**
 * BlockData metadata for sculk catalyst blocks.
 * Sets the bloom state.
 */
public record SculkCatalystMeta(boolean bloom) implements BlockDataMeta<SculkCatalyst> {

    @Override
    public void apply(SculkCatalyst blockData) {
        blockData.setBloom(bloom);
    }
}
