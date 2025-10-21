package fr.traqueur.items.settings;

import fr.traqueur.items.api.settings.Settings;
import fr.traqueur.structura.annotations.Options;

import java.util.List;

public record PluginSettings(
        boolean debug,
        @Options(optional = true) List<String> blockBreakEventPlugins
) implements Settings {
}
