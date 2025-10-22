package fr.traqueur.items.api.items;

import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import fr.traqueur.structura.api.Loadable;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
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
        List<EnchantmentSetting> enchantments,

        @Options(optional = true)
        List<AttributeSetting> attributes,

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
        @DefaultInt(-1)
        int maxStackSize,

        @Options(optional = true)
        @DefaultInt(-1) int repairCost,

        @Options(optional = true)
        Tag<DamageType> damageTypeResistance
) implements Loadable {

    /**
     * Represents an enchantment to be applied to an item.
     */
    public record EnchantmentSetting(
            @NotNull Enchantment enchantment,
            int level
    ) implements Loadable {
    }

    /**
     * Represents an attribute modifier to be applied to an item.
     */
    public record AttributeSetting(
            @NotNull Attribute attribute,
            @NotNull AttributeModifier.Operation operation,
            double amount,
            @NotNull EquipmentSlotGroup slot
    ) implements Loadable {
    }
}