package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultDouble;

public record XPBoostSettings(String name,
                              double xpBoost,
                              @Options(optional = true) @DefaultDouble(-1) double chanceToBoost) implements EffectSettings {
}
