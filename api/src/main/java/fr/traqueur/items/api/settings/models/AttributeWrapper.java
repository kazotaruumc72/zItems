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
 */
public record AttributeWrapper(
        @NotNull Attribute attribute,
        @NotNull AttributeModifier.Operation operation,
        double amount,
        @NotNull EquipmentSlotGroup slot
) implements Loadable {

    public AttributeModifier toAttributeModifier(ItemsPlugin plugin) {
        return new AttributeModifier(new NamespacedKey(plugin, UUID.randomUUID().toString()), amount, operation, slot);
    }

}