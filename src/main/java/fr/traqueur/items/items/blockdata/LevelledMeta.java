package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import org.bukkit.block.data.Levelled;

/**
 * BlockData metadata for levelled blocks (water, lava, cauldrons).
 * Sets the level.
 */
@BlockDataMetaMeta("levelled")
public record LevelledMeta(
        int level,
        @Options(optional = true) @DefaultBool(false) boolean max
) implements BlockDataMeta<Levelled> {

    @Override
    public void apply(Levelled blockData) {
        if (max) {
            blockData.setLevel(blockData.getMaximumLevel());
        } else {
            blockData.setLevel(level);
        }
    }
}
