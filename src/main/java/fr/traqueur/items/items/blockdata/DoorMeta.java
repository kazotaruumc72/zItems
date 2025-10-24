package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Door;

/**
 * BlockData metadata for door blocks.
 * Sets the door hinge side.
 */
@AutoBlockDataMeta("door")
public record DoorMeta(Door.Hinge hinge) implements BlockDataMeta<Door> {

    @Override
    public void apply(Door blockData) {
        blockData.setHinge(hinge);
    }
}
