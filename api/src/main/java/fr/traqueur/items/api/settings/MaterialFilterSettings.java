package fr.traqueur.items.api.settings;

import fr.traqueur.items.api.effects.EffectSettings;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;

import java.util.List;

/**
 * Extension of EffectSettings that provides material and tag-based filtering for blocks.
 * This interface can be used by any effect that needs to determine if a block should be processed
 * based on configured materials and tags.
 */
public interface MaterialFilterSettings extends EffectSettings {

    /**
     * Gets the list of materials to filter.
     *
     * @return List of materials, or null if not configured
     */
    List<Material> materials();

    /**
     * Gets the list of tags to filter.
     *
     * @return List of tags, or null if not configured
     */
    List<Tag<Material>> tags();

    /**
     * Determines if the filter is in blacklist mode.
     * - Blacklist mode (true): blocks IN the lists are NOT breakable
     * - Whitelist mode (false): only blocks IN the lists are breakable
     *
     * @return true for blacklist mode, false for whitelist mode
     */
    boolean blacklisted();

    /**
     * Checks if a block can be processed based on materials and tags configuration.
     * This default implementation provides the filtering logic.
     *
     * @param block The block to check
     * @return true if the block can be processed, false otherwise
     */
    default boolean isBreakable(Block block) {
        Material blockType = block.getType();

        // Don't process air or liquid
        if (blockType.isAir() || block.isLiquid()) {
            return false;
        }

        // Check if block is in materials list
        boolean inMaterials = materials() != null && materials().contains(blockType);

        // Check if block is in any of the tags
        boolean inTags = false;
        if (tags() != null) {
            for (Tag<Material> tag : tags()) {
                if (tag.isTagged(blockType)) {
                    inTags = true;
                    break;
                }
            }
        }

        boolean found = inMaterials || inTags;

        // Blacklist mode: return true if NOT found in lists
        // Whitelist mode: return true if found in lists
        return blacklisted() != found;
    }
}