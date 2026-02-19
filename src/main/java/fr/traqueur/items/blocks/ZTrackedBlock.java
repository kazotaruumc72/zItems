package fr.traqueur.items.blocks;

import fr.traqueur.items.api.blocks.TrackedBlock;

/**
 * Represents a tracked block with its packed position and associated custom item ID.
 *
 * @param packedPosition the packed block position within a chunk
 * @param itemId the custom item ID
 */
public record ZTrackedBlock(int packedPosition, String itemId) implements TrackedBlock {
}