package fr.traqueur.items.api.blocks;

/**
 * Represents a tracked block with its packed position and associated item ID.
 */
public interface TrackedBlock {

    /**
     * Gets the packed position of the tracked block.
     *
     * @return the packed position as an integer from a bit shift operation
     */
    int packedPosition();

    /**
     * Gets the item ID associated with the tracked block.
     *
     * @return the item ID as a string
     */
    String itemId();

}