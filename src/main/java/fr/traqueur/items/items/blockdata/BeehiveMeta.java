package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.Beehive;

/**
 * BlockData metadata for beehive blocks.
 * Sets the honey level.
 */
@AutoBlockDataMeta("beehive")
public record BeehiveMeta(int honeyLevel) implements BlockDataMeta<Beehive> {

    @Override
    public void apply(Beehive blockData) {
        if (honeyLevel >= 0 && honeyLevel <= blockData.getMaximumHoneyLevel()) {
            blockData.setHoneyLevel(honeyLevel);
        }
    }
}
