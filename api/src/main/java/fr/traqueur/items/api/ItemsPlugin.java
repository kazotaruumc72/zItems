package fr.traqueur.items.api;

import fr.maxlego08.menu.api.InventoryManager;
import fr.traqueur.items.api.effects.EffectsDispatcher;
import fr.traqueur.items.api.managers.Manager;
import fr.traqueur.recipes.api.RecipesAPI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ItemsPlugin extends JavaPlugin {

    public abstract RecipesAPI getRecipesManager();

    public abstract EffectsDispatcher getDispatcher();

    /**
     * Register a manager with the plugin.
     *
     * @param clazz   the interface/absract class of the manager
     * @param manager the implementation manager instance
     * @param <I>     the manager type
     * @return the manager instance
     */
    public <I extends Manager> I registerManager(Class<I> clazz, I manager) {
        this.getServer().getServicesManager().register(clazz, manager, this, ServicePriority.Normal);
        return manager;
    }

    /**
     * Get a manager instance of the plugin.
     *
     * @param clazz the interface/abstract class of the manager
     * @param <I>   the manager type
     * @return the manager instance
     */
    public <I extends Manager> I getManager(Class<I> clazz) {
        var rsp = this.getServer().getServicesManager().getRegistration(clazz);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }

    public abstract InventoryManager getInventoryManager();
}
