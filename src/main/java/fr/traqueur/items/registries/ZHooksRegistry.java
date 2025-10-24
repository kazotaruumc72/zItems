package fr.traqueur.items.registries;

import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.HooksRegistry;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZHooksRegistry implements HooksRegistry {

    private final Map<String, Hook> hooks;

    public ZHooksRegistry() {
        this.hooks = new HashMap<>();
    }

    @Override
    public void register(String s, Hook item) {
        this.hooks.put(s, item);
    }

    @Override
    public Hook getById(String s) {
        return this.hooks.get(s);
    }

    @Override
    public Collection<Hook> getAll() {
        return this.hooks.values();
    }

    @Override
    public void enableAll() {
        for (Map.Entry<String, Hook> stringHookEntry : this.hooks.entrySet()) {
            String hookName = stringHookEntry.getKey();
            Hook hook = stringHookEntry.getValue();
            if (Bukkit.getPluginManager().getPlugin(hookName) == null) {
                Logger.debug("Hook " + hookName + " not found, skipping...");
                continue;
            }
            hook.onEnable();
            Logger.debug("Enabled hook: " + hookName);
        }
    }
}
