package fr.traqueur.items.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ZEffectsRegistry implements EffectsRegistry {

    private static final String[] EXAMPLE_FILES = {
        "example_absorption.yml",
        "example_attributes.yml",
        "example_auto_sell.yml",
        "example_empty.yml",
        "example_enchants.yml",
        "example_farming_hoe.yml",
        "example_hammer.yml",
        "example_infinite_bucket.yml",
        "example_melt_mining.yml",
        "example_sell_stick.yml",
        "example_silk_spawner.yml",
        "example_unbreakable.yml",
        "example_vein_miner.yml",
        "example_xp_boost.yml"
    };

    private final Map<String, Effect> effects;
    private final ItemsPlugin plugin;

    public ZEffectsRegistry(ItemsPlugin plugin) {
        this.effects = new HashMap<>();
        this.plugin = plugin;
    }

    @Override
    public void register(String id, Effect item) {
        this.effects.put(id, item);
    }

    @Override
    public Effect getById(String id) {
        return this.effects.get(id);
    }

    @Override
    public Collection<Effect> getAll() {
        return this.effects.values();
    }

    @Override
    public void loadFromFolder(Path effectsFolder) {
        // Créer le dossier s'il n'existe pas et copier les exemples
        if (!Files.exists(effectsFolder)) {
            try {
                Files.createDirectories(effectsFolder);
                Logger.info("Created effects folder: " + effectsFolder);

                // Copier les fichiers exemples
                copyExampleFiles();
            } catch (IOException e) {
                Logger.severe("Failed to create effects folder: " + effectsFolder, e);
                return;
            }
        }

        if (!Files.isDirectory(effectsFolder)) {
            Logger.warning("Effects path is not a directory: " + effectsFolder);
            return;
        }

        // Charger tous les fichiers .yml/.yaml du dossier
        try (Stream<Path> files = Files.walk(effectsFolder)) {
            files.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                 .forEach(this::loadEffect);
        } catch (IOException e) {
            Logger.severe("Failed to read effects folder: " + effectsFolder, e);
        }

        Logger.info("Loaded " + this.effects.size() + " effect(s) from folder: " + effectsFolder);
    }

    /**
     * Copie les fichiers exemples depuis les resources vers le dossier effects
     */
    private void copyExampleFiles() {
        Logger.info("Copying example effect files...");

        int copied = 0;
        for (String fileName : EXAMPLE_FILES) {
            String resourcePath = "effects/" + fileName;
            plugin.saveResource(resourcePath, false);
        }

        Logger.info("Copied " + copied + " example effect file(s)");
    }

    private void loadEffect(Path file) {
        try {
            Effect effect = Structura.load(file, Effect.class);
            this.register(effect.id(), effect);
            Logger.debug("Loaded effect: " + effect.id() + " from file: " + file.getFileName());
        } catch (StructuraException e) {
            Logger.severe("Failed to load effect from file: " + file.getFileName(), e);
        }
    }
}
