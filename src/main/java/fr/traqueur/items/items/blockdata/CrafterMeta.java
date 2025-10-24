package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import org.bukkit.block.Orientation;
import org.bukkit.block.data.type.Crafter;

/**
 * BlockData metadata for crafter blocks.
 * Sets crafting state, triggered state, and orientation.
 */
@BlockDataMetaMeta("crafter")
public record CrafterMeta(
        boolean crafting,
        boolean triggered,
        @Options(optional = true) Orientation orientation
) implements BlockDataMeta<Crafter> {

    @Override
    public void apply(Crafter blockData) {
        blockData.setCrafting(crafting);
        blockData.setTriggered(triggered);
        if (orientation != null) {
            blockData.setOrientation(orientation);
        }
    }
}
