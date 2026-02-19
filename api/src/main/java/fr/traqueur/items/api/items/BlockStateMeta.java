package fr.traqueur.items.api.items;

import fr.traqueur.structura.annotations.Polymorphic;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

/**
 * Polymorphic interface for BlockState settings.
 * Each implementation represents a specific type of BlockState that can be configured.
 * Unlike BlockDataMeta which handles BlockData properties, this handles tile entity data.
 *
 * @param <T> the specific BlockState type
 */
@Polymorphic(useKey = true)
public interface BlockStateMeta<T extends BlockState> extends Loadable {

    /**
     * Applies this setting to a BlockState instance.
     *
     * @param player     the player associated with the action (can be null)
     * @param blockState the BlockState to modify
     */
    void apply(Player player, T blockState);
}