package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.effects.Effect;

import java.nio.file.Path;

public interface EffectsRegistry extends Registry<String, Effect> {
    void loadFromFolder(Path effects);
}
