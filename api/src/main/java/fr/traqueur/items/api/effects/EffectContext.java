package fr.traqueur.items.api.effects;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public record EffectContext(Player executor, ItemStack itemSource, Event event,  Set<Block> affectedBlocks, List<ItemStack> drops) {

    public <T extends Event> T getEventAs(Class<T> eventClass) {
        if (eventClass.isInstance(event)) {
            return eventClass.cast(event);
        }
        throw new IllegalStateException("Event is not of type " + eventClass.getName());
    }

    public void addDrop(@NotNull ItemStack quantity) {
        drops.add(quantity);
    }

    public void addDrops(@NotNull Collection<ItemStack> quantities) {
        drops.addAll(quantities);
    }
}
