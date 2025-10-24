package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Hangable;

/**
 * BlockData metadata for hangable blocks (lanterns, etc.).
 * Sets whether the block is hanging.
 */
@BlockDataMetaMeta("hangable")
public record HangableMeta(boolean hanging) implements BlockDataMeta<Hangable> {

    @Override
    public void apply(Hangable blockData) {
        blockData.setHanging(hanging);
    }
}
