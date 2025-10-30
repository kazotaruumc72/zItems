package fr.traqueur.items.settings;

import fr.traqueur.items.api.settings.Settings;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultInt;

import java.util.List;

public record PluginSettings(
        boolean debug,
        @Options(optional = true) List<String> blockBreakEventPlugins,
        @Options(optional = true) @DefaultInt(-1) int defaultNbEffectsView
) implements Settings {
}
