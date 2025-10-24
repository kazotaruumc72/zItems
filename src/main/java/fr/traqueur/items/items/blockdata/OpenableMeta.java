package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Openable;

/**
 * BlockData metadata for openable blocks.
 * Sets whether the block is open.
 */
@BlockDataMetaMeta("openable")
public record OpenableMeta(boolean open) implements BlockDataMeta<Openable> {

    @Override
    public void apply(Openable blockData) {
        blockData.setOpen(open);
    }
}
