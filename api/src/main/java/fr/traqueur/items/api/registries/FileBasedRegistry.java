package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class FileBasedRegistry<ID, T> implements Registry<ID, T> {
    
    protected final ItemsPlugin plugin;
    protected final Map<ID, T> storage;
    private final String[] exampleFiles;
    private final String resourceFolder;
    private final String logName;
    
    protected FileBasedRegistry(ItemsPlugin plugin, String[] exampleFiles,
                                String resourceFolder, String logName) {
        this.plugin = plugin;
        this.storage = new HashMap<>();
        this.exampleFiles = exampleFiles;
        this.resourceFolder = resourceFolder;
        this.logName = logName;
    }
    
    public void loadFromFolder(Path folder) {
        if (!ensureFolderExists(folder)) {
            return;
        }
        
        if (!Files.isDirectory(folder)) {
            Logger.warning(logName + " path is not a directory: " + folder);
            return;
        }
        
        try (Stream<Path> files = Files.walk(folder)) {
            files.filter(Files::isRegularFile)
                 .filter(this::isYamlFile)
                 .forEach(this::loadFile);
        } catch (IOException e) {
            Logger.severe("Failed to read " + logName + " folder: " + folder, e);
        }
        
        Logger.info("Loaded " + storage.size() + " " + logName + "(s) from folder: " + folder);
    }
    
    private boolean ensureFolderExists(Path folder) {
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
                Logger.info("Created " + logName + " folder: " + folder);
                copyExampleFiles();
                return true;
            } catch (IOException e) {
                Logger.severe("Failed to create " + logName + " folder: " + folder, e);
                return false;
            }
        }
        return true;
    }
    
    private boolean isYamlFile(Path path) {
        String pathStr = path.toString().toLowerCase();
        return pathStr.endsWith(".yml") || pathStr.endsWith(".yaml");
    }
    
    private void copyExampleFiles() {
        Logger.info("Copying example " + logName + " files...");
        for (String fileName : exampleFiles) {
            String resourcePath = resourceFolder + "/" + fileName;
            plugin.saveResource(resourcePath, false);
        }
        Logger.info("Copied " + exampleFiles.length + " example " + logName + " file(s)");
    }
    
    protected abstract void loadFile(Path file);
    
    @Override
    public void register(ID id, T item) {
        storage.put(id, item);
    }
    
    @Override
    public T getById(ID id) {
        return storage.get(id);
    }
    
    @Override
    public Collection<T> getAll() {
        return storage.values();
    }
}