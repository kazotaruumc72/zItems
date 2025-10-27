package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.items.api.settings.models.EnchantmentWrapper;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public record EnchantsSettings(List<EnchantSetting> enchantments) implements EffectSettings {

    public enum Evolution {
        INCREASE,
        DECREASE
    }

    public record EnchantSetting(@Options(inline = true) EnchantmentWrapper wrapper, Evolution evolution) implements Loadable {

        public int computeEvolutionValue() {
            return switch (evolution) {
                case INCREASE -> wrapper.level();
                case DECREASE -> -wrapper.level();
            };
        }

    }

}
