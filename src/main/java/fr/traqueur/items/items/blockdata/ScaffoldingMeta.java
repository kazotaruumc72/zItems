package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.Scaffolding;

/**
 * BlockData metadata for scaffolding blocks.
 * Sets the bottom state and distance.
 */
@AutoBlockDataMeta("scaffolding")
public record ScaffoldingMeta(boolean bottom, int distance) implements BlockDataMeta<Scaffolding> {

    @Override
    public void apply(Scaffolding blockData) {
        blockData.setBottom(bottom);
        blockData.setDistance(distance);
    }
}
