package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Powerable;

/**
 * BlockData metadata for powerable blocks.
 * Sets whether the block is powered.
 */
@BlockDataMetaMeta("powerable")
public record PowerableMeta(boolean powered) implements BlockDataMeta<Powerable> {

    @Override
    public void apply(Powerable blockData) {
        blockData.setPowered(powered);
    }
}
