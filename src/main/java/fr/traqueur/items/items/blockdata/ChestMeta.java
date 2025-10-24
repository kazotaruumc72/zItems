package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Chest;

/**
 * BlockData metadata for chest blocks.
 * Sets the chest type (single, left, right).
 */
@AutoBlockDataMeta("chest")
public record ChestMeta(Chest.Type type) implements BlockDataMeta<Chest> {

    @Override
    public void apply(Chest blockData) {
        blockData.setType(type);
    }
}
