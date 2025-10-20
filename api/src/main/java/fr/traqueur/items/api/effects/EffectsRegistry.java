package fr.traqueur.items.api.effects;

import fr.traqueur.items.api.registries.Registry;

import java.nio.file.Path;

public interface EffectsRegistry extends Registry<Effect> {
    void loadFromFolder(Path effects);
}
