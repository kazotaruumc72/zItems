package fr.traqueur.items.registries;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.items.effects.ZEffect;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ZEffectsRegistry extends EffectsRegistry {

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

    public ZEffectsRegistry(ItemsPlugin plugin) {
        super(plugin, EXAMPLE_FILES);
    }


    @Override
    protected void loadFile(Path file) {
        try {
            Effect effect = Structura.load(file, ZEffect.class);
            this.register(effect.id(), effect);
            Logger.debug("Loaded effect: " + effect.id() + " from file: " + file.getFileName());
        } catch (StructuraException e) {
            Logger.severe("Failed to load effect from file: " + file.getFileName(), e);
        }
    }
}
