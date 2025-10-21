package fr.traqueur.items.api.serialization;

import fr.traqueur.items.api.effects.Effect;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class EffectDataType implements PersistentDataType<String, Effect> {

    public static EffectDataType INSTANCE;

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<Effect> getComplexType() {
        return Effect.class;
    }
}
