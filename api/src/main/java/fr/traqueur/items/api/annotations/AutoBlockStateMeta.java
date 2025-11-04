package fr.traqueur.items.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark {@link fr.traqueur.items.api.items.BlockStateMeta} implementations for automatic registration.
 * The value is used as the discriminator key for polymorphic deserialization.
 * <p>Example usage:</p>
 * <pre>{@code
 * @AutoBlockStateMeta("container")
 * public record ContainerStateMeta(...) implements BlockStateMeta<Container> {
 *     // ...
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoBlockStateMeta {

    /**
     * The unique identifier for this BlockStateMeta type.
     * Used as the discriminator key in YAML configuration.
     *
     * @return the BlockStateMeta type identifier
     */
    String value();
}