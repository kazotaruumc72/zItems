package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.TechnicalPiston;

/**
 * BlockData metadata for technical piston blocks.
 * Sets the piston type.
 */
@BlockDataMetaMeta("technical-piston")
public record TechnicalPistonMeta(TechnicalPiston.Type type) implements BlockDataMeta<TechnicalPiston> {

    @Override
    public void apply(TechnicalPiston blockData) {
        blockData.setType(type);
    }
}
