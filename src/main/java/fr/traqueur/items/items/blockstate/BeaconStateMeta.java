package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.structura.annotations.Options;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * BlockState configuration for beacon blocks.
 * Allows setting primary and secondary effects.
 */
@AutoBlockStateMeta("beacon")
public record BeaconStateMeta(
        @Options(optional = true) PotionEffectType primaryEffect,
        @Options(optional = true) PotionEffectType secondaryEffect
) implements BlockStateMeta<Beacon> {

    @Override
    public void apply(Player player, Beacon beacon) {
        if (primaryEffect != null) {
            beacon.setPrimaryEffect(primaryEffect);
        }

        if (secondaryEffect != null) {
            beacon.setSecondaryEffect(secondaryEffect);
        }
    }
}