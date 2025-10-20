package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.items.api.interactions.InteractionAction;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.annotations.defaults.DefaultDouble;
import org.bukkit.inventory.EquipmentSlot;

public record SellStickSettings(
        String name,
        @Options(optional = true) @DefaultDouble(1.0) double multiplier,
        @Options(optional = true) @DefaultBool(true) boolean damage,
        @Options(optional = true) InteractionAction action,
        @Options(optional = true) EquipmentSlot hand
) implements EffectSettings {
}