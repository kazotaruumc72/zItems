package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;

/**
 * BlockState configuration for brewing stand blocks.
 * Allows setting brew time and fuel level.
 */
@AutoBlockStateMeta("brewing-stand")
public record BrewingStandStateMeta(
        @Options(optional = true) @DefaultInt(-1) int brewingTime,
        @Options(optional = true) @DefaultInt(-1) int fuelLevel
) implements BlockStateMeta<BrewingStand> {

    @Override
    public void apply(Player player, BrewingStand brewingStand) {
        if (brewingTime >= 0) {
            brewingStand.setBrewingTime(brewingTime);
        }

        if (fuelLevel >= 0) {
            brewingStand.setFuelLevel(fuelLevel);
        }
    }
}