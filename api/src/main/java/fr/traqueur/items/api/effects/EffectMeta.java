package fr.traqueur.items.api.effects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark an EffectHandler for automatic registration.
 * The value represents the unique identifier for this effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EffectMeta {

    /**
     * The unique identifier for this effect.
     * This will be used as the key in the effects registry.
     *
     * @return the effect identifier
     */
    String value();
}
