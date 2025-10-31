package fr.traqueur.items.registries;

import fr.traqueur.items.PlatformType;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.annotations.AutoMetadata;
import fr.traqueur.items.api.items.BlockDataMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.items.ItemFolder;
import fr.traqueur.items.api.items.ItemMetadata;
import fr.traqueur.items.api.registries.HandlersRegistry;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.items.ZItem;
import fr.traqueur.items.utils.MessageUtil;
import fr.traqueur.items.utils.ReflectionsCache;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.registries.PolymorphicRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ZItemsRegistry extends ItemsRegistry {

    private ItemFolder rootFolder;

    public ZItemsRegistry(ItemsPlugin plugin) {
        super(plugin);
        Reflections reflections = ReflectionsCache.getInstance().getOrCreate(plugin, "fr.traqueur.items");

        PolymorphicRegistry.create(BlockDataMeta.class, registry -> {
            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(AutoBlockDataMeta.class);
            int count = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (!BlockDataMeta.class.isAssignableFrom(clazz)) {
                    Logger.warning("Class <yellow>{}<reset> is annotated with @BlockDataMetaMeta but does not implement BlockDataMeta. Skipping.",
                            clazz.getSimpleName());
                    continue;
                }
                AutoBlockDataMeta meta = clazz.getAnnotation(AutoBlockDataMeta.class);

                //noinspection unchecked
                registry.register(meta.value(), (Class<? extends BlockDataMeta<?>>) clazz);
                count++;
                Logger.debug("Registered BlockDataMeta type <aqua>{}<reset> with id <gold>{}<reset>.", clazz.getSimpleName(), meta.value());
            }
            Logger.info("Registered <gold>{}<reset> BlockDataMeta type(s).", count);
        });

        PolymorphicRegistry.create(BlockStateMeta.class, registry -> {
            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(AutoBlockStateMeta.class);
            int count = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (!BlockStateMeta.class.isAssignableFrom(clazz)) {
                    Logger.warning("Class <yellow>{}<reset> is annotated with @AutoBlockStateMeta but does not implement BlockStateMeta. Skipping.",
                            clazz.getSimpleName());
                    continue;
                }
                AutoBlockStateMeta meta = clazz.getAnnotation(AutoBlockStateMeta.class);

                //noinspection unchecked
                registry.register(meta.value(), (Class<? extends BlockStateMeta<?>>) clazz);
                count++;
                Logger.debug("Registered BlockStateMeta type <aqua>{}<reset> with id <gold>{}<reset>.", clazz.getSimpleName(), meta.value());
            }
            Logger.info("Registered <gold>{}<reset> BlockStateMeta type(s).", count);
        });

        PolymorphicRegistry.create(ItemMetadata.class, registry -> {
            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(AutoMetadata.class);
            int count = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (!ItemMetadata.class.isAssignableFrom(clazz)) {
                    Logger.warning("Class <yellow>{}<reset> is annotated with @MetadataMeta but does not implement ItemMetadata. Skipping.",
                            clazz.getSimpleName());
                    continue;
                }
                AutoMetadata meta = clazz.getAnnotation(AutoMetadata.class);

                boolean paperOnly = clazz.isAnnotationPresent(AutoMetadata.PaperMetadata.class);
                boolean spigotOnly = clazz.isAnnotationPresent(AutoMetadata.SpigotMetadata.class);
                if (spigotOnly && PlatformType.isPaper()) {
                    Logger.debug("Skipping registration of ItemMetadata type <aqua>{}<reset> as it is Spigot-only.", clazz.getSimpleName());
                    continue;
                }

                if (paperOnly && !PlatformType.isPaper()) {
                    Logger.debug("Skipping registration of ItemMetadata type <aqua>{}<reset> as it is Paper-only.", clazz.getSimpleName());
                    continue;
                }

                //noinspection unchecked
                registry.register(meta.value(), (Class<? extends ItemMetadata>) clazz);
                count++;
                Logger.debug("Registered ItemMetadata type <aqua>{}<reset> with id <gold>{}<reset>.", clazz.getSimpleName(), meta.value());
            }
            Logger.info("Registered <gold>{}<reset> ItemMetadata type(s).", count);
        });

    }

    @Override
    protected Item loadFile(Path file) {
        try {
            Item item = Structura.load(file, ZItem.class);

            // Validate effects compatibility before registering
            if (!validateEffectsCompatibility(item, file)) {
                Logger.severe("Failed to load item {} from file {}: incompatible effects detected",
                        item.id(), file.getFileName());
                return null;
            }

            this.register(item.id(), item);
            Logger.debug("Loaded item: " + item.id() + " from file: " + file.getFileName());
            return item;
        } catch (StructuraException e) {
            Logger.severe("Failed to load item from file: " + file.getFileName(), e);
            return null;
        }
    }

    /**
     * Validates that all effects in the item settings are compatible with each other.
     *
     * @param item the item to validate
     * @param file the file path for logging purposes
     * @return true if all effects are compatible, false otherwise
     */
    private boolean validateEffectsCompatibility(Item item, Path file) {
        List<Effect> effects = item.settings().effects();
        if (effects == null || effects.isEmpty()) {
            return true; // No effects to validate
        }

        HandlersRegistry registry = Registry.get(HandlersRegistry.class);
        List<EffectHandler<?>> handlers = new ArrayList<>();

        // Get all handlers
        for (Effect effect : effects) {
            EffectHandler<?> handler = registry.getById(effect.type());
            if (handler == null) {
                Logger.warning("Handler not found for effect type {} in item {} ({})",
                        effect.type(), item.id(), file.getFileName());
                return false;
            }
            handlers.add(handler);
        }

        // Check each pair of handlers for incompatibility
        for (int i = 0; i < handlers.size(); i++) {
            EffectHandler<?> handler1 = handlers.get(i);
            Set<Class<? extends EffectHandler<?>>> incompatibles1 = handler1.getIncompatibleHandlers();

            for (int j = i + 1; j < handlers.size(); j++) {
                EffectHandler<?> handler2 = handlers.get(j);

                // Check if handler1 is incompatible with handler2
                if (incompatibles1.contains(handler2.getClass())) {
                    Logger.severe("Incompatible effects in item {} ({}): {} is incompatible with {}",
                            item.id(), file.getFileName(),
                            effects.get(i).type(), effects.get(j).type());
                    return false;
                }

                // Check if handler2 is incompatible with handler1 (bidirectional check)
                Set<Class<? extends EffectHandler<?>>> incompatibles2 = handler2.getIncompatibleHandlers();
                if (incompatibles2.contains(handler1.getClass())) {
                    Logger.severe("Incompatible effects in item {} ({}): {} is incompatible with {}",
                            item.id(), file.getFileName(),
                            effects.get(j).type(), effects.get(i).type());
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void loadFromFolder(Path folder) {
        if(!this.ensureFolderExists(folder)) {
            return;
        }
        // Clear existing items
        this.storage.clear();

        // Build folder structure and load items
        this.rootFolder = buildFolderStructure(folder, folder);

        Logger.info("Loaded {} items in folder structure", this.storage.size());
    }

    /**
     * Recursively builds the folder structure and loads items.
     *
     * @param currentPath the current directory being processed
     * @param rootPath the root items folder
     * @return the ItemFolder representing the current directory
     */
    private ItemFolder buildFolderStructure(Path currentPath, Path rootPath) {
        List<ItemFolder> subFolders = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        String folderName = currentPath.equals(rootPath) ? "root" : currentPath.getFileName().toString();
        String displayName = folderName;
        Material material = Material.CHEST;
        int modelId = -1;

        try (Stream<Path> paths = Files.list(currentPath)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    // Recursively build sub-folder
                    ItemFolder subFolder = buildFolderStructure(path, rootPath);
                    if (!subFolder.isEmpty()) {
                        subFolders.add(subFolder);
                    }
                } else if (path.toString().endsWith(".yml") || path.toString().endsWith(".yaml")) {
                    // Load item file and get the item back
                    items.add(loadFile(path));
                }
            });
        } catch (IOException e) {
            Logger.severe("Failed to read directory: " + currentPath, e);
        }

        Path folderPropertiesPath = currentPath.resolve("folder.properties");
        if (Files.exists(folderPropertiesPath)) {
            Properties properties = new Properties();
            try (InputStream in = Files.newInputStream(folderPropertiesPath)) {
                properties.load(in);
            } catch (IOException e) {
                Logger.severe("Failed to load folder properties from: " + folderPropertiesPath, e);
            }
            String materialStr = properties.getProperty("material");
            String modelIdStr = properties.getProperty("model-id", "-1");
            displayName = properties.getProperty("display-name");
            material = Material.matchMaterial(materialStr);
            try {
                modelId = Integer.parseInt(modelIdStr);
            } catch (NumberFormatException e) {
                Logger.warning("Invalid model-id '{}' in folder properties at {}. Using default -1.", modelIdStr, folderPropertiesPath);
            }
        }

        // Get folder name
        return new ItemFolder(folderName, displayName, material, modelId, subFolders, items);
    }

    @Override
    public ItemFolder getRootFolder() {
        return rootFolder != null ? rootFolder : new ItemFolder("root", "root", Material.CHEST, -1, List.of(), List.of());
    }
}