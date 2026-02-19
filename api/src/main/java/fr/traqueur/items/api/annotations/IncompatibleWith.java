package fr.traqueur.items.api.annotations;

import fr.traqueur.items.api.effects.EffectHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark effect handlers that are incompatible with other effect handlers.
 * When multiple effects are applied to an item, the system will check for incompatibilities
 * and prevent incompatible effects from being applied together.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @AutoEffect("VEIN_MINER")
 * @IncompatibleWith(HammerHandler.class)
 * public class VeinMinerHandler implements EffectHandler<VeinMinerSettings> {
 *     // ...
 * }
 * }</pre>
 *
 * <p><strong>Note:</strong> Incompatibility is bidirectional. If A is incompatible with B,
 * then B is automatically incompatible with A.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IncompatibleWith {

    /**
     * Array of effect handler classes that are incompatible with this handler.
     *
     * @return array of incompatible effect handler classes
     */
    Class<? extends EffectHandler<?>>[] value();
}