package fr.traqueur.items.api.items;

import fr.traqueur.items.api.settings.ItemSettings;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a custom item in the plugin.
 * This interface follows the Effect/ZEffect pattern for consistent architecture.
 */
public interface Item {

    /**
     * Gets the unique identifier for this item.
     *
     * @return the item's unique ID
     */
    @NotNull
    String id();

    /**
     * Gets the configuration/settings for this item.
     *
     * @return the item's settings
     */
    @NotNull
    ItemSettings settings();

    default Component representativeName() {
        if(settings().displayName() == null  && settings().itemName() == null) {
            return Component.text(id());
        }
        if(settings().displayName() != null) {
            return settings().displayName();
        } else {
            return settings().itemName();
        }
    }

    /**
     * Builds an ItemStack for this item.
     *
     * @param player the player to build the item for (can be null for placeholder resolution)
     * @param amount the stack size
     * @return the built ItemStack
     */
    @NotNull
    ItemStack build(@Nullable Player player, int amount);
}