package fr.traqueur.items.api.effects;

import fr.traqueur.items.api.annotations.IncompatibleWith;
import org.bukkit.event.Event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an effect that can be applied in response to a specific event.
 * Effects can be multi-event or single-event.
 * This interface defines the contract for effects that can be registered and executed.
 */
public sealed interface EffectHandler<T extends EffectSettings> permits EffectHandler.MultiEventEffectHandler, EffectHandler.NoEventEffectHandler, EffectHandler.SingleEventEffectHandler {

    void handle(EffectContext context, T settings);

    int priority();

    /**
     * Returns the settings class type for this handler.
     * <p>
     * By default, this method uses reflection to extract the generic type parameter
     * from the implementing class, eliminating the need for manual implementation.
     * <p>
     * Handlers can override this method if needed, but it's generally not necessary.
     *
     * @return the Class object for the settings type
     */
    @SuppressWarnings("unchecked")
    default Class<T> settingsType() {
        // Try to get from generic interfaces
        Type[] genericInterfaces = getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();
                // Check if this is one of our handler interfaces
                if (rawType instanceof Class<?> rawClass &&
                        EffectHandler.class.isAssignableFrom(rawClass)) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        return (Class<T>) actualTypeArguments[0];
                    }
                }
            }
        }

        throw new IllegalStateException(
                "Could not resolve settings type for handler " + getClass().getName() +
                        ". Ensure the class directly implements one of the EffectHandler interfaces with concrete type parameters."
        );
    }

    default boolean canApply(Event event) {
        return switch (this) {
            case SingleEventEffectHandler<?, ?> singleEventEffectHandler ->
                    singleEventEffectHandler.eventType().isInstance(event);
            case MultiEventEffectHandler<?> multiEventEffectHandler ->
                    multiEventEffectHandler.eventTypes().stream().anyMatch(type -> type.isInstance(event));
            case NoEventEffectHandler<?> __ -> event == null;
        };
    }

    /**
     * Returns a set of effect handler classes that are incompatible with this handler.
     * This is determined by the {@link IncompatibleWith} annotation on the implementing class.
     *
     * @return set of incompatible effect handler classes, or empty set if none
     */
    default Set<Class<? extends EffectHandler<?>>> getIncompatibleHandlers() {
        IncompatibleWith annotation = this.getClass().getAnnotation(IncompatibleWith.class);
        if (annotation == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(annotation.value())
                .collect(Collectors.toSet());
    }

    non-sealed interface MultiEventEffectHandler<T extends EffectSettings> extends EffectHandler<T> {
        Set<Class<? extends Event>> eventTypes();
    }

    non-sealed interface SingleEventEffectHandler<T extends EffectSettings, E extends Event> extends EffectHandler<T> {

        @SuppressWarnings("unchecked")
        default Class<E> eventType() {
            Type[] genericInterfaces = getClass().getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType parameterizedType) {
                    Type rawType = parameterizedType.getRawType();
                    // Check if this is one of our handler interfaces
                    if (rawType instanceof Class<?> rawClass &&
                            SingleEventEffectHandler.class.isAssignableFrom(rawClass)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        if (actualTypeArguments.length > 0) {
                            return (Class<E>) actualTypeArguments[1];
                        }
                    }
                }
            }

            throw new IllegalStateException(
                    "Could not resolve event type for handler " + getClass().getName() +
                            ". Ensure the class directly implements one of the EffectHandler interfaces with concrete type parameters."
            );
        }
    }

    non-sealed interface NoEventEffectHandler<T extends EffectSettings> extends EffectHandler<T> {
    }

}