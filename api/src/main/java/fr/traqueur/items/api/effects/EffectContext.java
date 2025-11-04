package fr.traqueur.items.api.effects;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represents the context in which an effect is applied.
 *
 * @param executor      the player executing the effect
 * @param itemSource    the item stack that is the source of the effect
 * @param event         the event triggering the effect
 * @param affectedBlocks the set of blocks affected by the effect
 * @param drops         the list of item stacks dropped as a result of the effect
 */
public record EffectContext(Player executor, ItemStack itemSource, Event event, Set<Block> affectedBlocks,
                            List<ItemStack> drops) {

    /**
     * Retrieves the event cast to the specified class type.
     *
     * @param eventClass the class to cast the event to
     * @param <T>        the type of the event
     * @return the event cast to the specified type
     * @throws IllegalStateException if the event is not of the specified type
     */
    public <T extends Event> T getEventAs(Class<T> eventClass) {
        if (eventClass.isInstance(event)) {
            return eventClass.cast(event);
        }
        throw new IllegalStateException("Event is not of type " + eventClass.getName());
    }

    /**
     * Adds a single item stack to the drops list.
     *
     * @param quantity the item stack to add
     */
    public void addDrop(@NotNull ItemStack quantity) {
        drops.add(quantity);
    }

    /**
     * Adds a collection of item stacks to the drops list.
     *
     * @param quantities the collection of item stacks to add
     */
    public void addDrops(@NotNull Collection<ItemStack> quantities) {
        drops.addAll(quantities);
    }
}
