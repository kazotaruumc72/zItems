package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Piston;

/**
 * BlockData metadata for piston blocks.
 * Sets whether the piston is extended.
 */
@BlockDataMetaMeta("piston")
public record PistonMeta(boolean extended) implements BlockDataMeta<Piston> {

    @Override
    public void apply(Piston blockData) {
        blockData.setExtended(extended);
    }
}
