package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Scaffolding;

/**
 * BlockData metadata for scaffolding blocks.
 * Sets the bottom state and distance.
 */
public record ScaffoldingMeta(boolean bottom, int distance) implements BlockDataMeta<Scaffolding> {

    @Override
    public void apply(Scaffolding blockData) {
        blockData.setBottom(bottom);
        blockData.setDistance(distance);
    }
}
