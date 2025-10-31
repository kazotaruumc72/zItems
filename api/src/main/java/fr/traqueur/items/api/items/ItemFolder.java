package fr.traqueur.items.api.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

/**
 * Represents a folder containing items and sub-folders.
 * Used for organizing items in a hierarchical structure based on the file system.
 */
public class ItemFolder {


    private String displayName;
    private Material displayMaterial;
    private int displayModelId;

    private final String name;
    private final List<ItemFolder> subFolders;
    private final List<String> itemIds;

    public ItemFolder(String name, String displayName, Material displayMaterial, int displayModelId, List<ItemFolder> subFolders, List<String> itemIds) {
        this.name = name;
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
        this.displayModelId = displayModelId;
        this.subFolders = subFolders;
        this.itemIds = itemIds;
    }

    /**
     * Checks if this folder is empty (no items and no sub-folders).
     */
    public boolean isEmpty() {
        return (itemIds == null || itemIds.isEmpty()) &&
               (subFolders == null || subFolders.isEmpty());
    }

    public String name() {
        return name;
    }

    public void displayName(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public void displayMaterial(Material displayMaterial) {
        this.displayMaterial = displayMaterial;
    }

    public Material displayMaterial() {
        return displayMaterial;
    }

    public void displayModelId(int displayModelId) {
        this.displayModelId = displayModelId;
    }

    public int displayModelId() {
        return displayModelId;
    }

    public List<ItemFolder> subFolders() {
        return subFolders;
    }

    public List<String> itemIds() {
        return itemIds;
    }

    /**
     * Gets the total number of elements (items + sub-folders) in this folder.
     */
    public int getElementCount() {
        int count = 0;
        if (itemIds != null) count += itemIds.size();
        if (subFolders != null) count += subFolders.size();
        return count;
    }
}