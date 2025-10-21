package fr.traqueur.items.utils;

import fr.traqueur.items.api.Logger;
import fr.traqueur.items.settings.PluginSettings;
import fr.traqueur.items.api.settings.Settings;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import java.util.List;

public class EventUtil {

    public static boolean fireEvent(Event event, HandlerList handlers) {
        List<String> allowedPlugins = Settings.get(PluginSettings.class).blockBreakEventPlugins();
        if (allowedPlugins == null || allowedPlugins.isEmpty()) {
            return true;
        }
        RegisteredListener[] listeners =  handlers.getRegisteredListeners();;
        for (RegisteredListener listener : listeners) {
            if (!allowedPlugins.contains(listener.getPlugin().getName())) {
                continue;
            }
            try {
                listener.callEvent(event);
            } catch (Exception e) {
                Logger.severe("An error occurred while firing event " + event.getClass().getSimpleName() + " for plugin " + listener.getPlugin().getName(), e);
            }
        }
        if (event instanceof Cancellable cancellableEvent) {
            return !cancellableEvent.isCancelled();
        }
        return true;
    }

}
