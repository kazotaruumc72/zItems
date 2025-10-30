package fr.traqueur.items.api.settings;

import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.items.ItemMetadata;
import fr.traqueur.items.api.settings.models.AttributeMergeStrategy;
import fr.traqueur.items.api.settings.models.AttributeWrapper;
import fr.traqueur.items.api.settings.models.DisabledEnchantment;
import fr.traqueur.items.api.settings.models.EnchantmentWrapper;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import fr.traqueur.structura.api.Loadable;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Configuration settings for a custom item.
 * This record contains all the properties that define how an item appears and behaves.
 */
public record ItemSettings(
        @NotNull Material material,

        @Options(optional = true)
        Component displayName,

        @Options(optional = true)
        Component itemName,

        @Options(optional = true)
        List<Component> lore,

        @Options(optional = true)
        List<EnchantmentWrapper> enchantments,

        @Options(optional = true)
        List<DisabledEnchantment> disabledEnchantments,

        @Options(optional = true)
        List<AttributeWrapper> attributes,

        @Options(optional = true)
        List<Effect> effects,

        @Options(optional = true)
        ItemRarity rarity,

        @Options(optional = true)
        List<ItemFlag> flags,

        @Options(optional = true)
        List<ItemMetadata> metadata,

        @Options(optional = true)
        @DefaultInt(-1)
        int maxDamage,

        @Options(optional = true)
        @DefaultInt(-1)
        int customModelData,

        @Options(optional = true)
        @DefaultBool(false)
        boolean unbreakable,

        @Options(optional = true)
        @DefaultBool(false)
        boolean hideTooltip,

        @Options(optional = true)
        @DefaultBool(false)
        boolean grindstoneEnabled,

        @Options(optional = true)
        @DefaultInt(-1)
        int maxStackSize,

        @Options(optional = true)
        @DefaultInt(-1) int repairCost,

        @Options(optional = true)
        Tag<DamageType> damageTypeResistance,

        @Options(optional = true)
        RecipeSettings recipe,

        @Options(optional = true)
        @AttributeMergeStrategy.DefaultStrategy(AttributeMergeStrategy.REPLACE)
        AttributeMergeStrategy attributeMergeStrategy,

        @Options(optional = true)
        @DefaultBool(true)
        boolean trackable
) implements Loadable {
}