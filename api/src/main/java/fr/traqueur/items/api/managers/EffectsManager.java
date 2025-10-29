package fr.traqueur.items.api.managers;

import fr.traqueur.items.api.effects.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public non-sealed interface EffectsManager extends Manager {
    void applyEffect(Player player, ItemStack item, Effect effect);

    /**
     * Checks if an ItemStack has any custom effects.
     *
     * @param item the item to check
     * @return true if the item has at least one effect, false otherwise
     */
    boolean hasEffects(ItemStack item);
}
