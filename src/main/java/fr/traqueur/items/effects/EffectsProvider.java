package fr.traqueur.items.effects;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.effects.EffectMeta;
import fr.traqueur.items.api.effects.EffectSettings;
import fr.traqueur.structura.registries.PolymorphicRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Modifier;
import java.util.*;

public class EffectsProvider {

    private static volatile EffectsProvider instance;

    public static void initialize(ItemsPlugin plugin) {
        if (instance == null) {
            synchronized (EffectsProvider.class) {
                if (instance == null) {
                    instance = new EffectsProvider(plugin);
                }
            }
        }
    }

    public static EffectsProvider getInstance() {
        if (instance == null) {
            throw new IllegalStateException("EffectsProvider is not initialized. Call initialize() first.");
        }
        return instance;
    }

    private final Map<String, EffectHandler<?>> handlers;
    private final Set<String> scannedPackages;

    private EffectsProvider(ItemsPlugin plugin) {
        this.handlers = new HashMap<>();
        this.scannedPackages = new HashSet<>();

        // Créer le registry pour EffectSettings
        PolymorphicRegistry.create(EffectSettings.class, registry -> {
            // Registry vide au départ, sera rempli après scan
        });

        // Scanner votre package par défaut
        this.scanPackage(plugin, "fr.traqueur.items");
    }

    /**
     * Allows external plugins/libraries to register their own package for scanning.
     * Can be called after initialization to add more packages.
     *
     * @param packageName the package to scan for EffectHandlers
     */
    public void scanPackage(JavaPlugin plugin, String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            Logger.warning("Cannot scan null or empty package name.");
            return;
        }

        if (scannedPackages.contains(packageName)) {
            Logger.debug("Package {} already scanned, skipping.", packageName);
            return;
        }

        Logger.info("Scanning package <aqua>{}<reset> for EffectHandlers...", packageName);

        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .forPackage(packageName, plugin.getClass().getClassLoader())
                    .addClassLoaders(plugin.getClass().getClassLoader())
                    .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes));

            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(EffectMeta.class);

            int count = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (!EffectHandler.class.isAssignableFrom(clazz)) {
                    Logger.warning("Class <yellow>{}<reset> is annotated with @EffectMeta but does not implement EffectHandler. Skipping.",
                            clazz.getSimpleName());
                    continue;
                }
                //noinspection unchecked
                if (registerEffectHandler((Class<? extends EffectHandler<?>>) clazz)) {
                    count++;
                }
            }

            scannedPackages.add(packageName);
            Logger.info("Registered <gold>{}<reset> effect handler(s) from package {}.", count, packageName);

        } catch (Exception e) {
            Logger.severe("Failed to scan package {}: {}", e, packageName);
        }
    }

    /**
     * Manually register an effect handler instance.
     * Useful for dynamic registration without package scanning.
     *
     * @param effectId the effect identifier
     * @param handler the effect handler instance
     */
    public void registerHandler(String effectId, EffectHandler<?> handler) {
        if (effectId == null || effectId.trim().isEmpty()) {
            Logger.severe("Cannot register handler with null or empty effect ID.");
            return;
        }

        if (handler == null) {
            Logger.severe("Cannot register null handler for effect ID: {}", effectId);
            return;
        }

        if (this.handlers.containsKey(effectId)) {
            Logger.warning("Effect ID <yellow>{}<reset> is already registered. Overwriting with {}.",
                    effectId, handler.getClass().getSimpleName());
        }

        this.handlers.put(effectId, handler);
        Logger.info("Manually registered effect handler: <aqua>{}<reset> -> {}",
                effectId, handler.getClass().getSimpleName());

        // Enregistrer le EffectSettings dans le registry polymorphique
        registerHandlerSettings(handler);
    }

    /**
     * Registers a single effect handler class.
     * @return true if successfully registered, false otherwise
     */
    private boolean registerEffectHandler(Class<? extends EffectHandler<?>> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
            Logger.debug("Class {} is abstract or an interface. Skipping.", clazz.getSimpleName());
            return false;
        }

        try {
            EffectMeta meta = clazz.getAnnotation(EffectMeta.class);
            String effectId = meta.value();

            if (this.handlers.containsKey(effectId)) {
                Logger.warning("Effect ID <yellow>{}<reset> is already registered. Skipping class {}.",
                        effectId, clazz.getSimpleName());
                return false;
            }

            EffectHandler<?> handler = clazz.getDeclaredConstructor().newInstance();

            this.handlers.put(effectId, handler);
            Logger.debug("Registered effect handler: <aqua>{}<reset> -> {}", effectId, clazz.getSimpleName());

            // Enregistrer le EffectSettings dans le registry polymorphique
            registerHandlerSettings(handler);

            return true;

        } catch (NoSuchMethodException e) {
            Logger.severe("Effect handler {} must have a no-args constructor.", clazz.getSimpleName());
            return false;
        } catch (Exception e) {
            Logger.severe("Failed to instantiate effect handler: {}", e, clazz.getName());
            return false;
        }
    }

    /**
     * Registers the settings class of a handler in the PolymorphicRegistry.
     */
    private void registerHandlerSettings(EffectHandler<?> handler) {
        Class<? extends EffectSettings> settingsClass = handler.settingsType();
        if (settingsClass != null) {
            try {
                PolymorphicRegistry<EffectSettings> registry = PolymorphicRegistry.get(EffectSettings.class);

                // Vérifier si déjà enregistré
                String settingsName = settingsClass.getSimpleName().toLowerCase();
                if (registry.get(settingsName).isPresent()) {
                    Logger.debug("EffectSettings class {} already registered.", settingsClass.getSimpleName());
                    return;
                }

                registry.register(settingsClass);
                Logger.debug("Registered EffectSettings class: <aqua>{}<reset>", settingsClass.getSimpleName());
            } catch (Exception e) {
                Logger.severe("Failed to register EffectSettings class: {}", e, settingsClass.getName());
            }
        }
    }

    /**
     * Gets a registered effect handler by its identifier.
     *
     * @param effectId the effect identifier
     * @return the effect handler, or null if not found
     */
    public EffectHandler<?> getHandler(String effectId) {
        return this.handlers.get(effectId);
    }

    /**
     * Gets all registered effect handlers.
     *
     * @return an unmodifiable view of all registered handlers
     */
    public Map<String, EffectHandler<?>> getHandlers() {
        return Collections.unmodifiableMap(this.handlers);
    }

    /**
     * Gets all scanned packages.
     *
     * @return an unmodifiable view of all scanned packages
     */
    public Set<String> getScannedPackages() {
        return Collections.unmodifiableSet(this.scannedPackages);
    }
}