package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.annotations.EffectMeta;
import fr.traqueur.items.api.effects.EffectContext;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.effects.settings.AttributesSettings;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;

import java.util.UUID;

@EffectMeta(value = "ATTRIBUTES_APPLICATOR")
public record AttributesApplicator(ItemsPlugin plugin) implements EffectHandler.NoEventEffectHandler<AttributesSettings> {

    @Override
    public void handle(EffectContext context, AttributesSettings settings) {
        context.itemSource().editMeta(meta -> {
            for (AttributesSettings.AttributeSetting setting : settings.attributes()) {
                meta.addAttributeModifier(
                        setting.attribute(),
                        new AttributeModifier(new NamespacedKey(plugin, UUID.randomUUID().toString()),
                                setting.amount(),
                                setting.operation(),
                                setting.slot()));
            }
        });
    }

    @Override
    public int priority() {
         return 1;
    }

    @Override
    public Class<AttributesSettings> settingsType() {
        return AttributesSettings.class;
    }
}
