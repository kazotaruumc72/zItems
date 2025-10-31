package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public abstract class FileBasedRegistry<ID, T> implements Registry<ID, T> {

    protected final ItemsPlugin plugin;
    protected final Map<ID, T> storage;
    private final String resourceFolder;
    private final String logName;

    protected FileBasedRegistry(ItemsPlugin plugin, String resourceFolder, String logName) {
        this.plugin = plugin;
        this.storage = new HashMap<>();
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

    protected boolean ensureFolderExists(Path folder) {
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
        List<String> copiedFiles = new ArrayList<>();

        try {
            List<String> resourceFiles = listResourceFiles(resourceFolder);

            for (String relativePath : resourceFiles) {
                if (isFile(relativePath)) {
                    try {
                        plugin.saveResource(resourceFolder + "/" + relativePath, false);
                        copiedFiles.add(relativePath);
                        Logger.debug("Copied example file: " + relativePath);
                    } catch (Exception e) {
                        Logger.warning("Failed to copy example file: " + relativePath + " - " + e.getMessage());
                    }
                }
            }

            Logger.info("Copied " + copiedFiles.size() + " example " + logName + " file(s)");
        } catch (Exception e) {
            Logger.severe("Failed to copy example " + logName + " files", e);
        }
    }


    private List<String> listResourceFiles(String folder) throws IOException {
        List<String> filePaths = new ArrayList<>();

        ClassLoader classLoader = plugin.getClass().getClassLoader();
        URI uri;
        try {
            uri = Objects.requireNonNull(classLoader.getResource(folder)).toURI();
        } catch (Exception e) {
            Logger.warning("Could not find resource folder: " + folder);
            return filePaths;
        }

        Path resourcePath;
        FileSystem fileSystem = null;
        boolean needsClose = false;

        try {
            if ("jar".equals(uri.getScheme())) {
                try {
                    fileSystem = FileSystems.getFileSystem(uri);
                } catch (FileSystemNotFoundException e) {
                    fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    needsClose = true;
                }
                resourcePath = fileSystem.getPath("/" + folder);
            } else {
                resourcePath = Paths.get(uri);
            }

            try (Stream<Path> paths = Files.walk(resourcePath)) {
                paths.filter(Files::isRegularFile)
                        .forEach(path -> {
                            // Conserve le chemin relatif complet
                            Path relative = resourcePath.relativize(path);
                            filePaths.add(relative.toString().replace("\\", "/"));
                        });
            }

        } finally {
            if (needsClose && fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    Logger.debug("Error closing file system: " + e.getMessage());
                }
            }
        }

        return filePaths;
    }


    private boolean isFile(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".yml") || lower.endsWith(".yaml") || lower.endsWith(".properties");
    }

    /**
     * Loads an object from a file and returns it.
     *
     * @param file the file to load
     * @return the loaded object, or null if loading failed
     */
    protected abstract T loadFile(Path file);

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

    @Override
    public void clear() {
        storage.clear();
    }
}