package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Leaves;

/**
 * BlockData metadata for leaves blocks.
 * Sets persistence and distance from log.
 */
public record LeavesMeta(boolean persistent, int distance) implements BlockDataMeta<Leaves> {

    @Override
    public void apply(Leaves blockData) {
        blockData.setPersistent(persistent);
        blockData.setDistance(distance);
    }
}
