package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.effects.EffectContext;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.effects.EffectMeta;
import fr.traqueur.items.effects.settings.EnchantsSettings;

@EffectMeta(value = "ENCHANTS_APPLICATOR")
public class EnchantsApplicator implements EffectHandler.NoEventEffectHandler<EnchantsSettings> {

    @Override
    public void handle(EffectContext context, EnchantsSettings settings) {
        boolean error = false;
        for (EnchantsSettings.EnchantSetting enchantment : settings.enchantments()) {
            int evolution = enchantment.computeEvolutionValue();
            int level = context.itemSource().getItemMeta().getEnchants().getOrDefault(enchantment.enchantment(), 0);
            if (level == 0) {
                error = true;
                break;
            }
            if(evolution < 0 && level + evolution < 0) {
                error = true;
                break;
            }
        }

        if (error) {
            return;
        }

        context.itemSource().editMeta(meta -> {
            for (EnchantsSettings.EnchantSetting enchantment : settings.enchantments()) {
                int evolution = enchantment.computeEvolutionValue();
                int level = meta.getEnchants().getOrDefault(enchantment.enchantment(), 0);

                if (level + evolution == 0) {
                    meta.removeEnchant(enchantment.enchantment());
                } else {
                    meta.addEnchant(enchantment.enchantment(), level + evolution, true);
                }
            }
        });
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Class<EnchantsSettings> settingsType() {
        return null;
    }
}
