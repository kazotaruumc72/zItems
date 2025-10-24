package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import org.bukkit.block.data.type.Sapling;

/**
 * BlockData metadata for sapling blocks.
 * Sets the growth stage of the sapling.
 */
@BlockDataMetaMeta("sapling")
public record SaplingMeta(
        int stage,
        @Options(optional = true) @DefaultBool(false) boolean max
) implements BlockDataMeta<Sapling> {

    @Override
    public void apply(Sapling blockData) {
        if (max) {
            blockData.setStage(blockData.getMaximumStage());
        } else {
            blockData.setStage(stage);
        }
    }
}
