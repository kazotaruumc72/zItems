package fr.traqueur.items.items;

import fr.traqueur.items.PlatformType;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.managers.EffectsManager;
import fr.traqueur.items.api.settings.ItemSettings;
import fr.traqueur.items.api.settings.models.EnchantmentWrapper;
import fr.traqueur.items.effects.ZEffectsManager;
import fr.traqueur.items.serialization.Keys;
import fr.traqueur.items.utils.AttributeUtil;
import fr.traqueur.items.utils.ItemUtil;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.api.Loadable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record ZItem(String id, @Options(inline = true) ItemSettings settings) implements Item, Loadable {

    @Override
    public @NotNull ItemStack build(@Nullable Player player, int amount) {
        ItemsPlugin plugin = JavaPlugin.getPlugin(ItemsPlugin.class);

        // Generate effect lore lines (base effects only during item creation)
        List<Component> effectLoreLines = List.of();
        if (settings.effects() != null && !settings.effects().isEmpty()) {
            EffectsManager effectsManager = plugin.getManager(EffectsManager.class);
            effectLoreLines = effectsManager.generateBaseEffectLore(settings.effects(), settings);
        }

        // Combine base lore with effect lore
        List<Component> combinedLore = new ArrayList<>();
        if (settings.lore() != null) {
            combinedLore.addAll(settings.lore());
        }
        combinedLore.addAll(effectLoreLines);

        // Create base item using ItemUtil
        ItemStack itemStack = ItemUtil.createItem(
                settings.material(),
                amount,
                settings.displayName(),
                combinedLore,
                settings.itemName()
        );

        AttributeUtil.applyAttributes(itemStack, settings.attributes(), plugin, settings.attributeMergeStrategy());

        // Apply additional settings via editMeta
        itemStack.editMeta(meta -> {
            // Apply enchantments
            if (settings.enchantments() != null) {
                for (EnchantmentWrapper enchantment : settings.enchantments()) {
                    meta.addEnchant(enchantment.enchantment(), enchantment.level(), true);
                }
            }

            if (meta instanceof Damageable damageable) {
                if (settings.maxDamage() > 0) {
                    damageable.setMaxDamage(settings.maxDamage());
                }
            }

            if (settings.customModelData() > 0) {
                if (PlatformType.isPaper()) {
                    meta.getCustomModelDataComponent()
                            .setFloats(List.of((float) settings.customModelData()));
                } else {
                    meta.setCustomModelData(settings.customModelData());
                }
            }

            meta.setUnbreakable(settings.unbreakable());

            meta.setHideTooltip(settings.hideTooltip());

            if (settings.maxStackSize() > 0) {
                meta.setMaxStackSize(settings.maxStackSize());
            }

            if (settings.rarity() != null) {
                meta.setRarity(settings.rarity());
            }

            if (settings.flags() != null) {
                meta.addItemFlags(settings.flags().toArray(ItemFlag[]::new));
            }

            if (meta instanceof Repairable repairable) {
                if (settings.repairCost() >= 0) {
                    repairable.setRepairCost(settings.repairCost());
                }
            }

            if (settings.damageTypeResistance() != null) {
                meta.setDamageResistant(settings.damageTypeResistance());
            }

        });

        if (settings.effects() != null && !settings.effects().isEmpty()) {
            EffectsManager effectsManager = plugin.getManager(EffectsManager.class);

            for (Effect effect : settings.effects()) {
                effectsManager.applyEffect(player, itemStack, effect);
            }
        }

        // Apply metadata
        if (settings.metadata() != null && !settings.metadata().isEmpty()) {
            for (var metadata : settings.metadata()) {
                metadata.apply(itemStack, player);
            }
        }

        itemStack.editPersistentDataContainer(container -> {
            Keys.ITEM_ID.set(container, id);
        });

        return itemStack;
    }
}