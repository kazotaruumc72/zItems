package fr.traqueur.items.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a Hook for automatic registration.
 * The value represents the plugin name that this hook integrates with.
 * <p>
 * Classes annotated with this annotation are automatically discovered and instantiated
 * via reflection during plugin initialization.
 * <p>
 * The hook will only be enabled if the corresponding plugin is present on the server.
 * <p>
 * <strong>Note to IDE users:</strong> Classes annotated with @AutoHook are used via reflection
 * and may appear as "unused" in the IDE. To suppress these warnings, configure your IDE to
 * recognize this annotation as an entry point annotation.
 *
 * <p>Example usage:
 * <pre>
 * {@code @AutoHook("WorldGuard")}
 * public class WorldGuardHook implements Hook {
 *     {@code @Override}
 *     public void onEnable() {
 *         // Hook initialization logic
 *     }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoHook {

    /**
     * The name of the plugin that this hook integrates with.
     * This should match the exact plugin name as it appears in the plugin.yml.
     * The hook will only be enabled if a plugin with this name is present.
     *
     * @return the plugin name
     */
    String value();
}