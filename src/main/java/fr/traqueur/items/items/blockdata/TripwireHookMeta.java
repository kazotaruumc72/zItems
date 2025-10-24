package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.TripwireHook;

/**
 * BlockData metadata for tripwire hook blocks.
 * Sets attached and powered state.
 */
@AutoBlockDataMeta("tripwire-hook")
public record TripwireHookMeta(boolean attached, boolean powered) implements BlockDataMeta<TripwireHook> {

    @Override
    public void apply(TripwireHook blockData) {
        blockData.setAttached(attached);
        blockData.setPowered(powered);
    }
}
