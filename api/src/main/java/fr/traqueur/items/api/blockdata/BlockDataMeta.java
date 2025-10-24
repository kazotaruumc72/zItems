package fr.traqueur.items.api.blockdata;

import fr.traqueur.structura.annotations.Polymorphic;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.block.data.BlockData;

/**
 * Polymorphic interface for BlockData settings.
 * Each implementation represents a specific type of BlockData property that can be configured.
 *
 * @param <T> the specific BlockData type
 */
@Polymorphic(useKey = true)
public interface BlockDataMeta<T extends BlockData> extends Loadable {

    /**
     * Applies this setting to a BlockData instance.
     *
     * @param blockData the BlockData to modify
     */
    void apply(T blockData);
}
