package fr.traqueur.items.api.managers;

import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectApplicationResult;
import fr.traqueur.items.api.settings.ItemSettings;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public non-sealed interface EffectsManager extends Manager {
    void loadRecipes();

    EffectApplicationResult applyEffect(Player player, ItemStack item, Effect effect);

    /**
     * Checks if an effect can be applied to the given item based on the effect's settings.
     *
     * @param effect the effect to check
     * @param item the item to apply to
     * @return true if the effect can be applied, false otherwise
     */
    boolean canApplyEffectTo(Effect effect, ItemStack item);

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

    /**
     * Checks if an ItemStack is an effect representation item (can be applied to equipment).
     *
     * @param item the item to check
     * @return true if this is an effect representation item
     */
    boolean isEffectItem(ItemStack item);

    /**
     * Gets the effect represented by this item, if any.
     *
     * @param item the item to check
     * @return the effect this item represents, or null if not an effect item
     */
    Effect getEffectFromItem(ItemStack item);

    /**
     * Creates an ItemStack that represents an effect and can be applied to equipment.
     *
     * @param effect the effect to create an item for
     * @param player the player context (for custom items)
     * @return the effect representation ItemStack, or null if effect has no representation
     */
    ItemStack createEffectItem(Effect effect, Player player);
}
