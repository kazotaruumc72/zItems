package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.TurtleEgg;

/**
 * BlockData metadata for turtle egg blocks.
 * Sets the number of eggs.
 */
@BlockDataMetaMeta("turtle-egg")
public record TurtleEggMeta(int eggs) implements BlockDataMeta<TurtleEgg> {

    @Override
    public void apply(TurtleEgg blockData) {
        int clampedEggs = Math.max(blockData.getMinimumEggs(), Math.min(eggs, blockData.getMaximumEggs()));
        blockData.setEggs(clampedEggs);
    }
}
