package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.PointedDripstone;

/**
 * BlockData metadata for pointed dripstone blocks.
 * Sets the thickness and vertical direction.
 */
@BlockDataMetaMeta("pointed-dripstone")
public record PointedDripstoneMeta(
        @Options(optional = true) PointedDripstone.Thickness thickness,
        @Options(optional = true) BlockFace verticalDirection
) implements BlockDataMeta<PointedDripstone> {

    @Override
    public void apply(PointedDripstone blockData) {
        if (thickness != null) {
            blockData.setThickness(thickness);
        }
        if (verticalDirection != null) {
            blockData.setVerticalDirection(verticalDirection);
        }
    }
}
