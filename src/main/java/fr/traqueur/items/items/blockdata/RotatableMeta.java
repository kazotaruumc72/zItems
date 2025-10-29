package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;

/**
 * BlockData metadata for rotatable blocks (signs, banners, skulls).
 * Sets the rotation.
 */
@AutoBlockDataMeta("rotatable")
public record RotatableMeta(BlockFace rotation) implements BlockDataMeta<Rotatable> {

    @Override
    public void apply(Rotatable blockData) {
        blockData.setRotation(rotation);
    }
}
