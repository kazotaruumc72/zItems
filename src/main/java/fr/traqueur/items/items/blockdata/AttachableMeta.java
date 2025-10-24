package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Attachable;

/**
 * BlockData metadata for attachable blocks.
 * Sets whether the block is attached.
 */
public record AttachableMeta(boolean attached) implements BlockDataMeta<Attachable> {

    @Override
    public void apply(Attachable blockData) {
        blockData.setAttached(attached);
    }
}
