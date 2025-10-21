package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.event.Event;

public interface ExtractorsRegistry extends Registry<Class<? extends Event>, ItemSourceExtractor<?>> {
    void registerDefaults();

    boolean has(Class<? extends Event> eventType);
}
