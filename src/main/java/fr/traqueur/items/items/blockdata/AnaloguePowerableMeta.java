package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import org.bukkit.block.data.AnaloguePowerable;

/**
 * BlockData metadata for analogue powerable blocks (like redstone comparators).
 * Sets the power level.
 */
@AutoBlockDataMeta("analogue-powerable")
public record AnaloguePowerableMeta(
        int power,
        @Options(optional = true) @DefaultBool(false) boolean max
) implements BlockDataMeta<AnaloguePowerable> {

    @Override
    public void apply(AnaloguePowerable blockData) {
        if (max) {
            blockData.setPower(blockData.getMaximumPower());
        } else {
            blockData.setPower(power);
        }
    }
}
