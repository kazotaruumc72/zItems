package fr.traqueur.items.api.effects;

import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.api.Loadable;

public record Effect(String id, String type, @Options(inline = true) EffectSettings settings) implements Loadable {
}
