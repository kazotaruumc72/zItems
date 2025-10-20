package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public record EnchantsSettings(List<EnchantSetting> enchantments) implements EffectSettings {

    public record EnchantSetting(Enchantment enchantment, Evolution evolution, int level) implements Loadable {

        public int computeEvolutionValue() {
            return switch (evolution) {
                case INCREASE -> level;
                case DECREASE -> -level;
            };
        }

    }

    public enum Evolution {
        INCREASE,
        DECREASE
    }

}
