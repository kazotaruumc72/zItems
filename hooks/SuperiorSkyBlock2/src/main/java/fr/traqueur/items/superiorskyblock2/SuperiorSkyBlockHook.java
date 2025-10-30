package fr.traqueur.items.superiorskyblock2;

import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.LocationAccessRegistry;
import fr.traqueur.items.api.registries.Registry;

@AutoHook("SuperiorSkyblock2")
public class SuperiorSkyBlockHook implements Hook {

    @Override
    public void onEnable() {
        Registry.get(LocationAccessRegistry.class).register("superiorskybock2", new SuperiorSkyBlockLocationAccess());
    }
}
