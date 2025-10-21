package fr.traqueur.items.effects;

import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.api.Loadable;

public record ZEffect(String id, String type, @Options(inline = true) EffectSettings settings) implements Effect, Loadable {
}
