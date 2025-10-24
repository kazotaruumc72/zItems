package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Tripwire;

/**
 * BlockData metadata for tripwire blocks.
 * Sets whether the tripwire is disarmed.
 */
@AutoBlockDataMeta("tripwire")
public record TripwireMeta(boolean disarmed) implements BlockDataMeta<Tripwire> {

    @Override
    public void apply(Tripwire blockData) {
        blockData.setDisarmed(disarmed);
    }
}
