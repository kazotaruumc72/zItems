package fr.traqueur.items.api.managers;

import fr.traqueur.items.api.effects.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public non-sealed interface EffectsManager extends Manager {
    void applyEffect(Player player, ItemStack item, Effect effect);
}
