package fr.traqueur.items.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark an {@link fr.traqueur.items.api.items.ItemMetadata} for automatic registration.
 * The value is used as the discriminator key for polymorphic deserialization.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoMetadata {

    /**
     * The unique identifier for this effect.
     * This will be used as the key in the effects registry.
     *
     * @return the effect identifier
     */
    String value();

    /**
     * Indicates if the metadata is only compatible with Paper.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface PaperMetadata {
    }

    /**
     * Indicates if the metadata is only compatible with Spigot.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface SpigotMetadata {
    }

}
