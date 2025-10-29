package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.hooks.Hook;
import org.bukkit.plugin.java.JavaPlugin;

public interface HooksRegistry extends Registry<String, Hook> {

    void enableAll();

    /**
     * Scans a package for classes annotated with @AutoHook and registers them.
     *
     * @param plugin the plugin instance
     * @param packageName the package name to scan
     */
    void scanPackage(JavaPlugin plugin, String packageName);

}
