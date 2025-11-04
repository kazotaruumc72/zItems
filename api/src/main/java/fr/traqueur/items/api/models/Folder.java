package fr.traqueur.items.api.models;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a folder that can contain sub-folders and elements of type T.
 *
 * @param displayName     The display name of the folder.
 * @param displayMaterial The material used to represent the folder visually.
 * @param displayModelId  The model ID for custom visual representation.
 * @param name            The unique name identifier for the folder.
 * @param subFolders      The list of sub-folders contained within this folder.
 * @param elements        The list of elements contained within this folder.
 * @param <T> The type of elements contained in the folder.
 */
public record Folder<T>(String name, String displayName, Material displayMaterial, int displayModelId,
                        List<Folder<T>> subFolders, List<T> elements) {

    /**
     * Checks if this folder is empty (no items and no sub-folders).
     * @return true if the folder is empty, false otherwise.
     */
    public boolean isEmpty() {
        return (elements == null || elements.isEmpty()) &&
                (subFolders == null || subFolders.isEmpty());
    }

}
