package fr.traqueur.items.api.items;

import fr.traqueur.structura.annotations.Polymorphic;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Polymorphic interface for item metadata configurations.
 * Each implementation represents a specific type of metadata that can be applied to an ItemStack.
 *
 * The discriminator key is determined by the YAML section name (e.g., "food", "leather-armor", "potion").
 */
@Polymorphic(useKey = true)
public interface ItemMetadata extends Loadable {

    /**
     * Applies this metadata to an ItemStack.
     *
     * @param itemStack the ItemStack to modify
     * @param player the player context (can be null)
     */
    void apply(ItemStack itemStack, @Nullable Player player);
}