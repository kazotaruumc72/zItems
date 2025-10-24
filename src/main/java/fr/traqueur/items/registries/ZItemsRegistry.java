package fr.traqueur.items.registries;

import fr.traqueur.items.PlatformType;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.annotations.AutoMetadata;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.items.ItemMetadata;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.items.ZItem;
import fr.traqueur.items.utils.ReflectionsCache;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.registries.PolymorphicRegistry;
import org.reflections.Reflections;

import java.nio.file.Path;
import java.util.Set;

public class ZItemsRegistry extends ItemsRegistry {

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
    protected void loadFile(Path file) {
        try {
            Item item = Structura.load(file, ZItem.class);
            this.register(item.id(), item);
            Logger.debug("Loaded item: " + item.id() + " from file: " + file.getFileName());
        } catch (StructuraException e) {
            Logger.severe("Failed to load item from file: " + file.getFileName(), e);
        }
    }
}