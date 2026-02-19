package fr.traqueur.items.api.settings.models;

import fr.traqueur.structura.api.Loadable;
import org.bukkit.enchantments.Enchantment;

/**
 * A wrapper for an enchantment and its level.
 *
 * @param enchantment the enchantment
 * @param level       the level of the enchantment
 */
public record EnchantmentWrapper(Enchantment enchantment, int level) implements Loadable {
}
