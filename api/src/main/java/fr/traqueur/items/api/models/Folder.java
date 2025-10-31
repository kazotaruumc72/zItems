package fr.traqueur.items.api.models;

import org.bukkit.Material;

import java.util.List;

public class Folder<T> {

    private String displayName;

    private final Material displayMaterial;
    private final int displayModelId;
    private final String name;
    private final List<Folder<T>> subFolders;
    private final List<T> elements;

    public Folder(String name, String displayName, Material displayMaterial, int displayModelId, List<Folder<T>> subFolders, List<T> elements) {
        this.name = name;
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
        this.displayModelId = displayModelId;
        this.subFolders = subFolders;
        this.elements = elements;
    }

    /**
     * Checks if this folder is empty (no items and no sub-folders).
     */
    public boolean isEmpty() {
        return (elements == null || elements.isEmpty()) &&
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

    public List<Folder<T>> subFolders() {
        return subFolders;
    }

    public List<T> elements() {
        return elements;
    }

}
