package fr.traqueur.items.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark an EffectHandler for automatic registration.
 * The value represents the unique identifier for this effect.
 * <p>
 * Classes annotated with this annotation are automatically discovered and instantiated
 * via reflection during plugin initialization.
 * <p>
 * <strong>Note to IDE users:</strong> Classes annotated with @EffectMeta are used via reflection
 * and may appear as "unused" in the IDE. To suppress these warnings, configure your IDE to
 * recognize this annotation as an entry point annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoEffect {

    /**
     * The unique identifier for this effect.
     * This will be used as the key in the effects registry.
     *
     * @return the effect identifier
     */
    String value();

    /**
     * Marker annotation for Paper-only effect handlers.
     * Effect handlers marked with this annotation will only be registered
     * when running on Paper or Paper-based servers.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface PaperEffect {
    }

    /**
     * Marker annotation for Spigot-only effect handlers.
     * Effect handlers marked with this annotation will only be registered
     * when running on Spigot (non-Paper) servers.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface SpigotEffect {
    }
}
