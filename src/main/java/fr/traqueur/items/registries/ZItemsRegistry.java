package fr.traqueur.items.registries;

import fr.traqueur.items.PlatformType;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.annotations.ExtractorMeta;
import fr.traqueur.items.api.annotations.MetadataMeta;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.items.ItemMetadata;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.items.ZItem;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.registries.PolymorphicRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ZItemsRegistry implements ItemsRegistry {

    private static final String[] EXAMPLE_FILES = {
        "example_super_sword.yml",
        "example_hammer_pickaxe.yml",
        "example_strength_sword.yml",
        "example_auto_sell_pickaxe.yml",
        "example_farming_hoe.yml",
        "example_legendary_axe.yml",
        "example_simple_stick.yml",
        "example_golden_apple.yml",
        "example_leather_armor.yml",
        "example_custom_potion.yml",
        "example_trimmed_armor.yml"
    };

    private final Map<String, Item> items;
    private final ItemsPlugin plugin;

    public ZItemsRegistry(ItemsPlugin plugin) {
        this.items = new HashMap<>();
        this.plugin = plugin;

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage("fr.traqueur.items.items.metadata", plugin.getClass().getClassLoader())
                .addClassLoaders(plugin.getClass().getClassLoader())
                .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes));

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(MetadataMeta.class);

        PolymorphicRegistry.create(ItemMetadata.class, registry -> {
            int count = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (!ItemMetadata.class.isAssignableFrom(clazz)) {
                    Logger.warning("Class <yellow>{}<reset> is annotated with @MetadataMeta but does not implement ItemMetadata. Skipping.",
                            clazz.getSimpleName());
                    continue;
                }
                MetadataMeta meta = clazz.getAnnotation(MetadataMeta.class);

                boolean paperOnly = clazz.isAnnotationPresent(MetadataMeta.PaperMetadata.class);
                boolean spigotOnly = clazz.isAnnotationPresent(MetadataMeta.SpigotMetadata.class);
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
    public void register(String id, Item item) {
        this.items.put(id, item);
    }

    @Override
    public Item getById(String id) {
        return this.items.get(id);
    }

    @Override
    public Collection<Item> getAll() {
        return this.items.values();
    }

    @Override
    public void loadFromFolder(Path itemsFolder) {
        // Créer le dossier s'il n'existe pas et copier les exemples
        if (!Files.exists(itemsFolder)) {
            try {
                Files.createDirectories(itemsFolder);
                Logger.info("Created items folder: " + itemsFolder);

                // Copier les fichiers exemples
                copyExampleFiles();
            } catch (IOException e) {
                Logger.severe("Failed to create items folder: " + itemsFolder, e);
                return;
            }
        }

        if (!Files.isDirectory(itemsFolder)) {
            Logger.warning("Items path is not a directory: " + itemsFolder);
            return;
        }

        try (Stream<Path> files = Files.walk(itemsFolder)) {
            files.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                 .forEach(this::loadItem);
        } catch (IOException e) {
            Logger.severe("Failed to read items folder: " + itemsFolder, e);
        }

        Logger.info("Loaded " + this.items.size() + " item(s) from folder: " + itemsFolder);
    }

    private void copyExampleFiles() {
        Logger.info("Copying example item files...");

        for (String fileName : EXAMPLE_FILES) {
            String resourcePath = "items/" + fileName;
            plugin.saveResource(resourcePath, false);
        }

        Logger.info("Copied " + EXAMPLE_FILES.length + " example item file(s)");
    }

    private void loadItem(Path file) {
        try {
            Item item = Structura.load(file, ZItem.class);
            this.register(item.id(), item);
            Logger.debug("Loaded item: " + item.id() + " from file: " + file.getFileName());
        } catch (StructuraException e) {
            Logger.severe("Failed to load item from file: " + file.getFileName(), e);
        }
    }
}