package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.Repeater;

/**
 * BlockData metadata for repeater blocks.
 * Sets the delay and locked state.
 */
@AutoBlockDataMeta("repeater")
public record RepeaterMeta(int delay, boolean locked) implements BlockDataMeta<Repeater> {

    @Override
    public void apply(Repeater blockData) {
        int clampedDelay = Math.max(blockData.getMinimumDelay(), Math.min(delay, blockData.getMaximumDelay()));
        blockData.setDelay(clampedDelay);
        blockData.setLocked(locked);
    }
}
