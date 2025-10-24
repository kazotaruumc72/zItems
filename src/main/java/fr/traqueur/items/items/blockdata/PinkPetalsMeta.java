package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.PinkPetals;

/**
 * BlockData metadata for pink petals blocks.
 * Sets the number of flowers.
 */
@BlockDataMetaMeta("pink-petals")
public record PinkPetalsMeta(int flowerAmount) implements BlockDataMeta<PinkPetals> {

    @Override
    public void apply(PinkPetals blockData) {
        blockData.setFlowerAmount(flowerAmount);
    }
}
