package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Snow;

/**
 * BlockData metadata for snow blocks.
 * Sets the number of layers.
 */
@BlockDataMetaMeta("snow")
public record SnowMeta(int layers) implements BlockDataMeta<Snow> {

    @Override
    public void apply(Snow blockData) {
        int clampedLayers = Math.max(blockData.getMinimumLayers(), Math.min(layers, blockData.getMaximumLayers()));
        blockData.setLayers(clampedLayers);
    }
}
