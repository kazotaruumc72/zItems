package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.items.api.settings.models.AttributeMergeStrategy;
import fr.traqueur.items.api.settings.models.AttributeWrapper;
import fr.traqueur.structura.annotations.Options;

import java.util.List;

public record AttributesSettings(List<AttributeWrapper> attributes, @Options(optional = true) @AttributeMergeStrategy.DefaultStrategy(AttributeMergeStrategy.REPLACE) AttributeMergeStrategy strategy) implements EffectSettings {

}
