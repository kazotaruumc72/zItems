package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Wall;

import java.util.Map;

/**
 * BlockData metadata for wall blocks.
 * Sets whether the wall has a post and the height of each side.
 */
@AutoBlockDataMeta("wall")
public record WallMeta(boolean up, Map<BlockFace, Wall.Height> heights) implements BlockDataMeta<Wall> {

    @Override
    public void apply(Wall blockData) {
        blockData.setUp(up);
        for (Map.Entry<BlockFace, Wall.Height> entry : heights.entrySet()) {
            blockData.setHeight(entry.getKey(), entry.getValue());
        }
    }
}
