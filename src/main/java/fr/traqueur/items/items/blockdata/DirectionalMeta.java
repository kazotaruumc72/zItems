package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;

/**
 * BlockData metadata for directional blocks.
 * Sets the facing direction of the block.
 */
@BlockDataMetaMeta("directional")
public record DirectionalMeta(BlockFace facing) implements BlockDataMeta<Directional> {

    @Override
    public void apply(Directional blockData) {
        blockData.setFacing(facing);
    }
}
