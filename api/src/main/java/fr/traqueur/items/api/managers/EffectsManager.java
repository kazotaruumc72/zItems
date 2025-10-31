package fr.traqueur.items.api.managers;

import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectApplicationResult;
import fr.traqueur.items.api.settings.ItemSettings;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public non-sealed interface EffectsManager extends Manager {
    EffectApplicationResult applyEffect(Player player, ItemStack item, Effect effect);

    /**
     * Checks if an ItemStack has any custom effects.
     *
     * @param item the item to check
     * @return true if the item has at least one effect, false otherwise
     */
    boolean hasEffects(ItemStack item);

    List<Component> generateBaseEffectLore(List<Effect> baseEffects, ItemSettings itemSettings);

    /**
     * Updates the item's lore to display the given effects.
     * Handles both custom items (with ItemSettings) and vanilla items.
     *
     * @param item the item to update
     * @param effects all effects to display in the lore
     */
    void updateItemLoreWithEffects(ItemStack item, List<Effect> effects);

    /**
     * Reapplies all NoEventEffects from the given list to the item.
     * This is useful when fusing items in anvils or similar scenarios.
     *
     * @param player the player context (can be null)
     * @param item the item to apply effects to
     * @param effects the list of effects to reapply
     */
    void reapplyNoEventEffects(Player player, ItemStack item, List<Effect> effects);
}
