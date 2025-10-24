package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.Axis;
import org.bukkit.block.data.Orientable;

/**
 * BlockData metadata for orientable blocks (logs, pillars, etc.).
 * Sets the orientation axis.
 */
public record OrientableMeta(Axis axis) implements BlockDataMeta<Orientable> {

    @Override
    public void apply(Orientable blockData) {
        if (blockData.getAxes().contains(axis)) {
            blockData.setAxis(axis);
        }
    }
}
