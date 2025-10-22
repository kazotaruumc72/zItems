package fr.traqueur.items.items.metadata;

import fr.traqueur.items.api.annotations.MetadataMeta;
import fr.traqueur.items.api.items.ItemMetadata;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.Nullable;

/**
 * Armor trim metadata configuration.
 * Discriminator key: "trim"
 */
@MetadataMeta("trim")
public record TrimMetadata(
        TrimMaterial material,
        TrimPattern pattern
) implements ItemMetadata {

    @Override
    public void apply(ItemStack itemStack, @Nullable Player player) {
        if (!(itemStack.getItemMeta() instanceof ArmorMeta meta)) {
            return;
        }

        ArmorTrim trim = new ArmorTrim(material, pattern);
        meta.setTrim(trim);
        itemStack.setItemMeta(meta);
    }
}