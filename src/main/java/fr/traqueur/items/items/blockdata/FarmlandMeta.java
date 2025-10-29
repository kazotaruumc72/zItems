package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import org.bukkit.block.data.type.Farmland;

/**
 * BlockData metadata for farmland blocks.
 * Sets the moisture level.
 */
@AutoBlockDataMeta("farmland")
public record FarmlandMeta(
        int moisture,
        @Options(optional = true) @DefaultBool(false) boolean max
) implements BlockDataMeta<Farmland> {

    @Override
    public void apply(Farmland blockData) {
        if (max) {
            blockData.setMoisture(blockData.getMaximumMoisture());
        } else {
            blockData.setMoisture(moisture);
        }
    }
}
