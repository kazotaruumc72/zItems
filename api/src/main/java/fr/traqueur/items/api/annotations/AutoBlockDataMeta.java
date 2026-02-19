package fr.traqueur.items.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark an {@link fr.traqueur.items.api.items.BlockDataMeta} for automatic registration.
 * The value is used as the discriminator key for polymorphic deserialization.
 * <p>Example usage:</p>
 * <pre>{@code
 * @AutoBlockDataMeta("ageable")
 * public record AgeableMeta(...) implements BlockDataMeta<Ageable> {
 *     // ...
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoBlockDataMeta {

    /**
     * The unique identifier for this BlockDataMeta type.
     * Used as the discriminator key in YAML configuration.
     *
     * @return the BlockDataMeta type identifier
     */
    String value();

}
