package fr.traqueur.items.api.effects;

import org.bukkit.event.Event;

import java.util.Set;

/**
 * Represents an effect that can be applied in response to a specific event.
 * Effects can be multi-event or single-event.
 * This interface defines the contract for effects that can be registered and executed.
 */
public sealed interface EffectHandler<T extends EffectSettings> permits EffectHandler.MultiEventEffectHandler, EffectHandler.NoEventEffectHandler, EffectHandler.SingleEventEffectHandler {

    void handle(EffectContext context, T settings);

    int priority();

    Class<T> settingsType();

    default boolean canApply(Event event) {
        return switch (this) {
            case SingleEventEffectHandler<?, ?> singleEventEffectHandler ->
                    singleEventEffectHandler.eventType().isInstance(event);
            case MultiEventEffectHandler<?> multiEventEffectHandler ->
                    multiEventEffectHandler.eventTypes().stream().anyMatch(type -> type.isInstance(event));
            case NoEventEffectHandler<?> __ -> event == null;
        };
    }

    non-sealed interface MultiEventEffectHandler<T extends EffectSettings> extends EffectHandler<T> {
        Set<Class<? extends Event>> eventTypes();
    }

    non-sealed interface SingleEventEffectHandler<T extends EffectSettings, E extends Event> extends EffectHandler<T> {
        Class<E> eventType();
    }

    non-sealed interface NoEventEffectHandler<T extends EffectSettings> extends EffectHandler<T> {
    }

}