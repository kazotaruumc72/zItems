package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.Lightable;

/**
 * BlockData metadata for lightable blocks (torches, campfires, etc.).
 * Sets whether the block is lit.
 */
@BlockDataMetaMeta("lightable")
public record LightableMeta(boolean lit) implements BlockDataMeta<Lightable> {

    @Override
    public void apply(Lightable blockData) {
        blockData.setLit(lit);
    }
}
