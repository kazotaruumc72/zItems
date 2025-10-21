package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.hooks.Hook;

public interface HooksRegistry extends Registry<String, Hook> {

    void enableAll();

}
