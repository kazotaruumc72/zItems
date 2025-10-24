package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.EndPortalFrame;

/**
 * BlockData metadata for end portal frame blocks.
 * Sets whether the frame has an eye of ender.
 */
@BlockDataMetaMeta("end-portal-frame")
public record EndPortalFrameMeta(boolean eye) implements BlockDataMeta<EndPortalFrame> {

    @Override
    public void apply(EndPortalFrame blockData) {
        blockData.setEye(eye);
    }
}
