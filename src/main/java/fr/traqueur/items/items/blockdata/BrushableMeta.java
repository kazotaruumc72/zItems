package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.Brushable;

/**
 * BlockData metadata for brushable blocks (suspicious sand/gravel).
 * Sets the dusted level.
 */
@AutoBlockDataMeta("brushable")
public record BrushableMeta(int dusted) implements BlockDataMeta<Brushable> {

    @Override
    public void apply(Brushable blockData) {
        if (dusted >= 0 && dusted <= blockData.getMaximumDusted()) {
            blockData.setDusted(dusted);
        }
    }
}
