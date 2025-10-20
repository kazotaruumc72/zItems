package fr.traqueur.items.effects.settings;

import fr.traqueur.items.api.settings.MaterialFilterSettings;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.List;

public record HammerSettings(
        String name,
        List<Material> materials,
        List<Tag<Material>> tags,
        @Options(optional = true) @DefaultBool(false) boolean blacklisted,
        int height,
        int width,
        int depth,
        @Options(optional = true) @DefaultInt(-1) int damage
) implements MaterialFilterSettings {
}
