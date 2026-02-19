package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.Hangable;

/**
 * BlockData metadata for hangable blocks (lanterns, etc.).
 * Sets whether the block is hanging.
 */
@AutoBlockDataMeta("hangable")
public record HangableMeta(boolean hanging) implements BlockDataMeta<Hangable> {

    @Override
    public void apply(Hangable blockData) {
        blockData.setHanging(hanging);
    }
}
