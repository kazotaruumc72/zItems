package fr.traqueur.items.api.settings.models;

import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.enchantments.Enchantment;

/**
 * Represents an enchantment that should be disabled for an item.
 * This prevents the enchantment from being applied via enchanting tables, anvils, or books.
 *
 * @param enchantment The specific enchantment to disable. If null, all enchantments are disabled.
 * @param level The specific level of the enchantment to disable. If -1, all levels are disabled.
 * <p>Examples:
 * <pre>
 * # Disable specific enchantment at any level
 * enchantment: MENDING
 * level: -1  # Any level
 *
 * # Disable specific enchantment at specific level
 * enchantment: SHARPNESS
 * level: 5  # Only level 5
 *
 * # Disable all enchantments at any level
 * enchantment: null
 * level: -1
 *
 * # Disable all enchantments at specific level
 * enchantment: null
 * level: 3  # All enchantments at level 3
 * </pre>
 */
public record DisabledEnchantment(
        @Options(optional = true) Enchantment enchantment,
        @Options(optional = true) @DefaultInt(-1) int level
) implements Loadable {

    /**
     * Checks if this disabled enchantment matches the given enchantment and level.
     *
     * @param enchant the enchantment to check
     * @param enchantLevel the enchantment level to check
     * @return true if this disabled enchantment matches the given enchantment and level
     */
    public boolean matches(Enchantment enchant, int enchantLevel) {
        boolean enchantmentMatches = (this.enchantment == null || enchant.equals(this.enchantment));
        boolean levelMatches = (this.level == -1 || enchantLevel == this.level);
        return enchantmentMatches && levelMatches;
    }
}