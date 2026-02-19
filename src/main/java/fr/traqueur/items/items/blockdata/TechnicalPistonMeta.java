package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.TechnicalPiston;

/**
 * BlockData metadata for technical piston blocks.
 * Sets the piston type.
 */
@AutoBlockDataMeta("technical-piston")
public record TechnicalPistonMeta(TechnicalPiston.Type type) implements BlockDataMeta<TechnicalPiston> {

    @Override
    public void apply(TechnicalPiston blockData) {
        blockData.setType(type);
    }
}
