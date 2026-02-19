package fr.traqueur.items.api.settings.models;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents an attribute modifier to be applied to an item.
 * Includes the attribute type, operation, amount, and equipment slot group.
 * @param attribute The type of attribute to modify.
 * @param operation The operation to apply to the attribute.
 * @param amount The amount to modify the attribute by.
 * @param slot The equipment slot group where the attribute modifier applies.
 */
public record AttributeWrapper(
        @NotNull Attribute attribute,
        @NotNull AttributeModifier.Operation operation,
        double amount,
        @NotNull EquipmentSlotGroup slot
) implements Loadable {

    /**
     * Converts this AttributeWrapper into a Bukkit AttributeModifier.
     *
     * @param plugin The ItemsPlugin instance used to create a unique NamespacedKey.
     * @return A new AttributeModifier based on the data in this AttributeWrapper.
     */
    public AttributeModifier toAttributeModifier(ItemsPlugin plugin) {
        return new AttributeModifier(new NamespacedKey(plugin, UUID.randomUUID().toString()), amount, operation, slot);
    }

}