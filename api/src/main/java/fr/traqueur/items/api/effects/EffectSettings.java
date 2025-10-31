package fr.traqueur.items.api.effects;

import fr.traqueur.structura.annotations.Polymorphic;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Polymorphic(inline = true)
public interface EffectSettings extends Loadable {

    /**
     * Gets the list of materials to which this effect can be applied.
     * If null or empty, the effect can be applied to any material (unless tags are specified).
     *
     * @return List of applicable materials, or null if not configured
     */
     @Nullable List<Material> applicableMaterials();

    /**
     * Gets the list of tags to which this effect can be applied.
     * If null or empty, the effect can be applied to any tag (unless materials are specified).
     *
     * @return List of applicable tags, or null if not configured
     */
     @Nullable List<Tag<Material>> applicableTags();

    /**
     * Determines if the applicability filter is in blacklist mode.
     * - Blacklist mode (true): items IN the lists CANNOT have this effect applied
     * - Whitelist mode (false): only items IN the lists CAN have this effect applied
     *
     * @return true for blacklist mode, false for whitelist mode (default: false)
     */
     boolean applicabilityBlacklisted();

    /**
     * Checks if this effect can be applied to the given material.
     * This default implementation provides the filtering logic based on materials, tags, and blacklist mode.
     *
     * @param material The material to check
     * @return true if the effect can be applied, false otherwise
     */
    default boolean canApplyTo(Material material) {
        List<Material> materials = applicableMaterials();
        List<Tag<Material>> tags = applicableTags();

        // If nothing is configured, allow everything
        if ((materials == null || materials.isEmpty()) && (tags == null || tags.isEmpty())) {
            return true;
        }

        // Check if material is in materials list
        boolean inMaterials = materials != null && materials.contains(material);

        // Check if material is in any of the tags
        boolean inTags = false;
        if (tags != null) {
            for (Tag<Material> tag : tags) {
                if (tag.isTagged(material)) {
                    inTags = true;
                    break;
                }
            }
        }

        boolean found = inMaterials || inTags;

        // Blacklist mode: return true if NOT found in lists
        // Whitelist mode: return true if found in lists
        return applicabilityBlacklisted() != found;
    }

    /**
     * Checks if this effect can be applied to the given item.
     *
     * @param item The item to check
     * @return true if the effect can be applied, false otherwise
     */
    default boolean canApplyTo(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        return canApplyTo(item.getType());
    }
}
