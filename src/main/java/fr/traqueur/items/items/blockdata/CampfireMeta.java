package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Campfire;

/**
 * BlockData metadata for campfire blocks.
 * Sets whether it's a signal fire.
 */
@BlockDataMetaMeta("campfire")
public record CampfireMeta(boolean signalFire) implements BlockDataMeta<Campfire> {

    @Override
    public void apply(Campfire blockData) {
        blockData.setSignalFire(signalFire);
    }
}
