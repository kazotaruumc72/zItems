package fr.traqueur.items.api;

import fr.traqueur.items.api.effects.EffectsDispatcher;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ItemsPlugin extends JavaPlugin {
    public abstract EffectsDispatcher getDispatcher();
}
