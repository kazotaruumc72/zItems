package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;

/**
 * BlockState configuration for furnace blocks (furnace, blast furnace, smoker).
 * Allows setting burn time and cook time.
 */
@AutoBlockStateMeta("furnace")
public record FurnaceStateMeta(
        @Options(optional = true) @DefaultInt(-1) int burnTime,
        @Options(optional = true) @DefaultInt(-1) int cookTime,
        @Options(optional = true) @DefaultInt(-1) int cookTimeTotal
) implements BlockStateMeta<Furnace> {

    @Override
    public void apply(Player player, Furnace furnace) {
        if (burnTime >= 0) {
            furnace.setBurnTime((short) burnTime);
        }

        if (cookTime >= 0) {
            furnace.setCookTime((short) cookTime);
        }

        if (cookTimeTotal >= 0) {
            furnace.setCookTimeTotal(cookTimeTotal);
        }
    }
}