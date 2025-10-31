package fr.traqueur.items.api.items;

import fr.traqueur.items.api.ItemsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

/**
 * Represents a folder containing items and sub-folders.
 * Used for organizing items in a hierarchical structure based on the file system.
 */
public class ItemFolder {


    private String displayName;

    private final Material displayMaterial;
    private final int displayModelId;
    private final String name;
    private final List<ItemFolder> subFolders;
    private final List<Item> items;

    public ItemFolder(String name, String displayName, Material displayMaterial, int displayModelId, List<ItemFolder> subFolders, List<Item> items) {
        this.name = name;
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
        this.displayModelId = displayModelId;
        this.subFolders = subFolders;
        this.items = items;
    }

    /**
     * Checks if this folder is empty (no items and no sub-folders).
     */
    public boolean isEmpty() {
        return (items == null || items.isEmpty()) &&
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

    public Material displayMaterial() {
        return displayMaterial;
    }

    public int displayModelId() {
        return displayModelId;
    }

    public List<ItemFolder> subFolders() {
        return subFolders;
    }

    public List<Item> items() {
        return items;
    }

}