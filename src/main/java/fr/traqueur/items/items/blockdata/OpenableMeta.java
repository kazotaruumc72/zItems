package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.Openable;

/**
 * BlockData metadata for openable blocks.
 * Sets whether the block is open.
 */
@AutoBlockDataMeta("openable")
public record OpenableMeta(boolean open) implements BlockDataMeta<Openable> {

    @Override
    public void apply(Openable blockData) {
        blockData.setOpen(open);
    }
}
