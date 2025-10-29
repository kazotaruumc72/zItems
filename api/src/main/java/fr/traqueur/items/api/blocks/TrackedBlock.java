package fr.traqueur.items.api.blocks;

/**
 * Represents a tracked block with its packed position and associated custom item ID.
 *
 */
public interface TrackedBlock {

    int packedPosition();

    String itemId();

}