package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.effects.EffectHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * Registry for EffectHandlers.
 * <p>
 * This registry is responsible for:
 * <ul>
 *   <li>Discovering EffectHandlers via package scanning</li>
 *   <li>Storing handlers by their effect ID (from @EffectMeta annotation)</li>
 *   <li>Managing polymorphic EffectSettings registration</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>{@code
 * HandlersRegistry registry = Registry.get(HandlersRegistry.class);
 *
 * // Scan a package for handlers
 * registry.scanPackage(plugin, "com.example.effects");
 *
 * // Get a handler by ID
 * EffectHandler<?> handler = registry.getById("HAMMER");
 * }</pre>
 */
public interface HandlersRegistry extends Registry<String, EffectHandler<?>> {

    /**
     * Scans a package for classes annotated with @EffectMeta and registers them.
     * <p>
     * This method will:
     * <ol>
     *   <li>Find all classes annotated with @EffectMeta in the package</li>
     *   <li>Instantiate handlers (tries constructor with JavaPlugin, then no-args)</li>
     *   <li>Register the handler by its effect ID</li>
     *   <li>Register the handler's settings class in the polymorphic registry</li>
     * </ol>
     *
     * @param plugin      the plugin instance (used for instantiation and classloader)
     * @param packageName the package to scan (e.g., "fr.traqueur.items.effects")
     */
    void scanPackage(JavaPlugin plugin, String packageName);

    /**
     * Gets all packages that have been scanned by this registry.
     *
     * @return an unmodifiable set of package names
     */
    Set<String> getScannedPackages();
}