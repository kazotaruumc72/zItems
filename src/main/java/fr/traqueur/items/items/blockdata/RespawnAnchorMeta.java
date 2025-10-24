package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.RespawnAnchor;

/**
 * BlockData metadata for respawn anchor blocks.
 * Sets the charge level.
 */
@BlockDataMetaMeta("respawn-anchor")
public record RespawnAnchorMeta(int charges) implements BlockDataMeta<RespawnAnchor> {

    @Override
    public void apply(RespawnAnchor blockData) {
        blockData.setCharges(charges);
    }
}
