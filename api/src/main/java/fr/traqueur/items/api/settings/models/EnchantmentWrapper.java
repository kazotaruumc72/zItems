package fr.traqueur.items.api.settings.models;

import fr.traqueur.structura.api.Loadable;
import org.bukkit.enchantments.Enchantment;

public record EnchantmentWrapper(Enchantment enchantment, int level) implements Loadable {
}
