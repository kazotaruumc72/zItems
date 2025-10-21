package fr.traqueur.items.hooks;

import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.LocationAccessRegistry;
import fr.traqueur.items.api.registries.Registry;

public class SuperiorSkyBlockHook implements Hook {

    @Override
    public void onEnable() {
        Registry.get(LocationAccessRegistry.class).register("superiorskybock2", new SuperiorSkyBlockLocationAccess());
    }
}
