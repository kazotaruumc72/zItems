package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Gate;

/**
 * BlockData metadata for gate blocks.
 * Sets whether the gate is attached to a wall.
 */
public record GateMeta(boolean inWall) implements BlockDataMeta<Gate> {

    @Override
    public void apply(Gate blockData) {
        blockData.setInWall(inWall);
    }
}
