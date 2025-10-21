package fr.traqueur.items.api;

import fr.traqueur.items.api.effects.EffectsDispatcher;
import fr.traqueur.items.api.managers.Manager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ItemsPlugin extends JavaPlugin {

    public abstract EffectsDispatcher getDispatcher();

    /**
     * Register a manager with the plugin.
     * @param clazz the interface/absract class of the manager
     * @param manager the implementation manager instance
     * @return the manager instance
     * @param <I> the manager type
     */
    public <I extends Manager> I registerManager(Class<I> clazz, I manager) {
        this.getServer().getServicesManager().register(clazz, manager, this, ServicePriority.Normal);
        return manager;
    }

    /**
     * Get a manager instance of the plugin.
     * @param clazz the interface/abstract class of the manager
     * @return the manager instance
     * @param <I> the manager type
     */
    public <I extends Manager> I getManager(Class<I> clazz) {
        var rsp = this.getServer().getServicesManager().getRegistration(clazz);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }

}
