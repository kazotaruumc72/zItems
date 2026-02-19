package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.PistonHead;

/**
 * BlockData metadata for piston head blocks.
 * Sets whether the piston head is short.
 */
@AutoBlockDataMeta("piston-head")
public record PistonHeadMeta(boolean isShort) implements BlockDataMeta<PistonHead> {

    @Override
    public void apply(PistonHead blockData) {
        blockData.setShort(isShort);
    }
}
