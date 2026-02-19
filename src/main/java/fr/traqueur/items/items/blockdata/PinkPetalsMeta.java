package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.PinkPetals;

/**
 * BlockData metadata for pink petals blocks.
 * Sets the number of flowers.
 */
@AutoBlockDataMeta("pink-petals")
public record PinkPetalsMeta(int flowerAmount) implements BlockDataMeta<PinkPetals> {

    @Override
    public void apply(PinkPetals blockData) {
        blockData.setFlowerAmount(flowerAmount);
    }
}
