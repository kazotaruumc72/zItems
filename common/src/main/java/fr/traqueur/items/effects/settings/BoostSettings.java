package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultDouble;

public record BoostSettings(double boost,
                            @Options(optional = true) @DefaultDouble(-1) double chanceToBoost) implements EffectSettings {

    public BoostSettings {
        if (boost < 0)
            throw new IllegalArgumentException("boost must be >= 0");
        if (chanceToBoost != -1 && (chanceToBoost < 0 || chanceToBoost > 100))
            throw new IllegalArgumentException("chanceToBoost must be between 0 and 100");
    }

}
