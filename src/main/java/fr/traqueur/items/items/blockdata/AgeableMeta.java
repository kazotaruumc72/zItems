package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import org.bukkit.block.data.Ageable;

/**
 * BlockData metadata for ageable blocks (crops, plants, etc.).
 * Sets the age of the block.
 */
@AutoBlockDataMeta("ageable")
public record AgeableMeta(
        int age,
        @Options(optional = true) @DefaultBool(false) boolean max
) implements BlockDataMeta<Ageable> {

    @Override
    public void apply(Ageable blockData) {
        if (max) {
            blockData.setAge(blockData.getMaximumAge());
        } else {
            blockData.setAge(age);
        }
    }
}
