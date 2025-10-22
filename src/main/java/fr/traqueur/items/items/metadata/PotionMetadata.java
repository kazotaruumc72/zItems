package fr.traqueur.items.items.metadata;

import fr.traqueur.items.api.annotations.MetadataMeta;
import fr.traqueur.items.api.items.ItemMetadata;
import fr.traqueur.items.settings.models.PotionEffectSettings;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Potion metadata configuration for potions and tipped arrows.
 * Discriminator key: "potion"
 */
@MetadataMeta("potion")
public record PotionMetadata(
        @Options(optional = true)
        Color color,

        @Options(optional = true)
        PotionType basePotionType,

        @Options(optional = true)
        List<PotionEffectSettings> customEffects
) implements ItemMetadata {

    @Override
    public void apply(ItemStack itemStack, @Nullable Player player) {
        if (!(itemStack.getItemMeta() instanceof PotionMeta meta)) {
            return;
        }

        if (color != null) {
            meta.setColor(color);
        }

        if (basePotionType != null) {
            meta.setBasePotionType(basePotionType);
        }

        if (customEffects != null && !customEffects.isEmpty()) {
            for (PotionEffectSettings effectSetting : customEffects) {
                meta.addCustomEffect(effectSetting.toPotionEffect(), true);
            }
        }

        itemStack.setItemMeta(meta);
    }
}