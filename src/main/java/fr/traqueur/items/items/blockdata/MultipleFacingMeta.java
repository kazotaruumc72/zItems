package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;

import java.util.Map;

/**
 * BlockData metadata for multiple facing blocks (fences, glass panes, etc.).
 * Sets which faces are connected.
 */
@AutoBlockDataMeta("multiple-facing")
public record MultipleFacingMeta(Map<BlockFace, Boolean> faces) implements BlockDataMeta<MultipleFacing> {

    @Override
    public void apply(MultipleFacing blockData) {
        for (Map.Entry<BlockFace, Boolean> entry : faces.entrySet()) {
            if (blockData.getAllowedFaces().contains(entry.getKey())) {
                blockData.setFace(entry.getKey(), entry.getValue());
            }
        }
    }
}
