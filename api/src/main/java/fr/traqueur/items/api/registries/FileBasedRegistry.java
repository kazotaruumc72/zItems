package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.models.Folder;
import org.bukkit.Material;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public abstract class FileBasedRegistry<ID, T> implements Registry<ID, T> {

    protected final ItemsPlugin plugin;
    protected final Map<ID, T> storage;
    private final String resourceFolder;
    private final String logName;

    // Nouveau : stockage du dossier racine
    private Folder<T> rootFolder;

    protected FileBasedRegistry(ItemsPlugin plugin, String resourceFolder, String logName) {
        this.plugin = plugin;
        this.storage = new HashMap<>();
        this.resourceFolder = resourceFolder;
        this.logName = logName;
    }

    // --- Méthode principale de chargement ---
    public void loadFromFolder(Path folder) {
        if (!ensureFolderExists(folder)) {
            return;
        }

        this.storage.clear();
        this.rootFolder = buildFolderStructure(folder, folder);

        Logger.info("Loaded {} {}(s) in folder structure", this.storage.size(), logName);
    }

    // --- Construction récursive des dossiers ---
    private Folder<T> buildFolderStructure(Path currentPath, Path rootPath) {
        List<Folder<T>> subFolders = new ArrayList<>();
        List<T> elements = new ArrayList<>();

        String folderName = currentPath.equals(rootPath) ? "root" : currentPath.getFileName().toString();
        String displayName = folderName;
        Material material = Material.CHEST;
        int modelId = -1;

        try (Stream<Path> paths = Files.list(currentPath)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    Folder<T> sub = buildFolderStructure(path, rootPath);
                    if (!sub.isEmpty()) {
                        subFolders.add(sub);
                    }
                } else if (isYamlFile(path)) {
                    T element = loadFile(path);
                    if (element != null) {
                        elements.add(element);
                    }
                }
            });
        } catch (IOException e) {
            Logger.severe("Failed to read directory: " + currentPath, e);
        }

        // Lecture des propriétés facultatives
        Path folderPropertiesPath = currentPath.resolve("folder.properties");
        if (Files.exists(folderPropertiesPath)) {
            Properties properties = new Properties();
            try (InputStream in = Files.newInputStream(folderPropertiesPath)) {
                properties.load(in);
            } catch (IOException e) {
                Logger.severe("Failed to load folder properties from: " + folderPropertiesPath, e);
            }

            String matStr = properties.getProperty("material");
            String modelStr = properties.getProperty("model-id", "-1");
            displayName = properties.getProperty("display-name", folderName);

            if (matStr != null) {
                Material m = Material.matchMaterial(matStr);
                if (m != null) material = m;
            }

            try {
                modelId = Integer.parseInt(modelStr);
            } catch (NumberFormatException e) {
                Logger.warning("Invalid model-id '{}' in folder properties at {}. Using default -1.",
                        modelStr, folderPropertiesPath);
            }
        }

        return new Folder<>(folderName, displayName, material, modelId, subFolders, elements);
    }

    // --- Utilitaires existants ---
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
        String name = path.toString().toLowerCase();
        return name.endsWith(".yml") || name.endsWith(".yaml");
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

    // --- Méthodes abstraites ---
    protected abstract T loadFile(Path file);

    @Override
    public void register(ID id, T value) {
        storage.put(id, value);
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

    // --- Nouveau getter pour le système de dossiers ---
    public Folder<T> getRootFolder() {
        return rootFolder != null
                ? rootFolder
                : new Folder<>("root", "root", Material.CHEST, -1, List.of(), List.of());
    }
}
