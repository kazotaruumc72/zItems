package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Snowable;

/**
 * BlockData metadata for snowable blocks (grass, podzol, etc.).
 * Sets whether the block is snowy.
 */
@BlockDataMetaMeta("snowable")
public record SnowableMeta(boolean snowy) implements BlockDataMeta<Snowable> {

    @Override
    public void apply(Snowable blockData) {
        blockData.setSnowy(snowy);
    }
}
