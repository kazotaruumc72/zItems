package fr.traqueur.items.items;

import fr.traqueur.items.PlatformType;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.managers.EffectsManager;
import fr.traqueur.items.api.settings.ItemSettings;
import fr.traqueur.items.serialization.Keys;
import fr.traqueur.items.utils.ItemUtil;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public record ZItem(String id, @Options(inline = true) ItemSettings settings) implements Item, Loadable {

    @Override
    public @NotNull ItemStack build(@Nullable Player player, int amount) {
        ItemsPlugin plugin = JavaPlugin.getPlugin(ItemsPlugin.class);

        // Create base item using ItemUtil
        ItemStack itemStack = ItemUtil.createItem(
                settings.material(),
                amount,
                settings.displayName(),
                settings.lore(),
                settings.itemName()
        );

        // Apply additional settings via editMeta
        itemStack.editMeta(meta -> {
            // Apply enchantments
            if (settings.enchantments() != null) {
                for (ItemSettings.EnchantmentSetting enchantment : settings.enchantments()) {
                    meta.addEnchant(enchantment.enchantment(), enchantment.level(), true);
                }
            }

            // Apply attributes
            if (settings.attributes() != null) {
                for (ItemSettings.AttributeSetting attribute : settings.attributes()) {
                    AttributeModifier modifier = new AttributeModifier(
                            new NamespacedKey(plugin, UUID.randomUUID().toString()),
                            attribute.amount(),
                            attribute.operation(),
                            attribute.slot()
                    );
                    meta.addAttributeModifier(attribute.attribute(), modifier);
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