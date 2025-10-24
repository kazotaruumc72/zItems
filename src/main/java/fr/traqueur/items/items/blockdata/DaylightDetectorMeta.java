package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.DaylightDetector;

/**
 * BlockData metadata for daylight detector blocks.
 * Sets whether the detector is inverted.
 */
@AutoBlockDataMeta("daylight-detector")
public record DaylightDetectorMeta(boolean inverted) implements BlockDataMeta<DaylightDetector> {

    @Override
    public void apply(DaylightDetector blockData) {
        blockData.setInverted(inverted);
    }
}
