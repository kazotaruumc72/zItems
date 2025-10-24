package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;

import java.util.List;

public record AttributesSettings(List<AttributeSetting> attributes) implements EffectSettings {

    public record AttributeSetting(Attribute attribute, AttributeModifier.Operation operation, double amount,
                                   EquipmentSlotGroup slot) implements Loadable {
    }

}
