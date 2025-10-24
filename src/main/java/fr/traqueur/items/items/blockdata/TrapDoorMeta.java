package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.TrapDoor;

/**
 * BlockData metadata for trapdoor blocks.
 * Sets whether the trapdoor is open.
 */
public record TrapDoorMeta(boolean open) implements BlockDataMeta<TrapDoor> {

    @Override
    public void apply(TrapDoor blockData) {
        blockData.setOpen(open);
    }
}
