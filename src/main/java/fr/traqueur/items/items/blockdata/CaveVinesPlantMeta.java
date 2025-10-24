package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.CaveVinesPlant;

/**
 * BlockData metadata for cave vines plant blocks.
 * Sets whether the vines have berries.
 */
public record CaveVinesPlantMeta(boolean berries) implements BlockDataMeta<CaveVinesPlant> {

    @Override
    public void apply(CaveVinesPlant blockData) {
        blockData.setBerries(berries);
    }
}
