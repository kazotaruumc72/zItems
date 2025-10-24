package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import org.bukkit.block.data.Hatchable;

/**
 * BlockData metadata for hatchable blocks (turtle eggs).
 * Sets the hatch level.
 */
@BlockDataMetaMeta("hatchable")
public record HatchableMeta(
        int hatch,
        @Options(optional = true) @DefaultBool(false) boolean max
) implements BlockDataMeta<Hatchable> {

    @Override
    public void apply(Hatchable blockData) {
        if (max) {
            blockData.setHatch(blockData.getMaximumHatch());
        } else {
            blockData.setHatch(hatch);
        }
    }
}
