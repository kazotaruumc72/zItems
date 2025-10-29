package fr.traqueur.items.api.serialization;

import fr.traqueur.items.api.blocks.TrackedBlock;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * PersistentDataType for serializing TrackedBlock objects.
 * <p>
 * Converts TrackedBlock to/from PersistentDataContainer for storage.
 */
public abstract class TrackedBlockDataType implements PersistentDataType<PersistentDataContainer, TrackedBlock> {

    public static TrackedBlockDataType INSTANCE;

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<TrackedBlock> getComplexType() {
        return TrackedBlock.class;
    }
}