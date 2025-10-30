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
}
