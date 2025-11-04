package fr.traqueur.items.api;

import fr.maxlego08.menu.api.InventoryManager;
import fr.traqueur.items.api.effects.EffectsDispatcher;
import fr.traqueur.items.api.managers.Manager;
import fr.traqueur.recipes.api.RecipesAPI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for zItems, providing custom item framework for Paper/Spigot servers.
 *
 * <p>This abstract class serves as the plugin's entry point and provides centralized access
 * to core systems including managers, registries, the effect dispatcher, and third-party
 * integrations (zMenu, RecipesAPI).</p>
 *
 * <h2>Plugin Architecture</h2>
 * <p>The plugin follows a modular architecture with clear separation of concerns:</p>
 * <ul>
 *   <li><b>API Module:</b> Public interfaces and contracts (this module)</li>
 *   <li><b>Implementation:</b> Core plugin logic (src/)</li>
 *   <li><b>Hook Modules:</b> Optional third-party plugin integrations (hooks/)</li>
 * </ul>
 *
 * <h2>Initialization Lifecycle</h2>
 * <p>Typical plugin startup sequence:</p>
 * <ol>
 *   <li>Plugin enables and initializes registries</li>
 *   <li>Configuration files are loaded (config.yml, messages.yml)</li>
 *   <li>Items and effects are loaded from YAML (items/, effects/)</li>
 *   <li>Managers are created and registered via {@link #registerManager(Class, Manager)}</li>
 *   <li>Hooks are discovered and enabled via {@link fr.traqueur.items.api.registries.HooksRegistry}</li>
 *   <li>Effect handlers are scanned and registered</li>
 *   <li>Recipes are generated for custom items</li>
 *   <li>Event listeners and commands are registered</li>
 * </ol>
 *
 * <h2>Manager System</h2>
 * <p>Managers are service objects registered with Bukkit's {@code ServicesManager}.
 * They provide high-level API access to plugin subsystems:</p>
 * <ul>
 *   <li>{@link fr.traqueur.items.api.managers.ItemsManager} - Item building and recipes</li>
 *   <li>{@link fr.traqueur.items.api.managers.EffectsManager} - Effect application and lore</li>
 * </ul>
 *
 * <h2>Registry System</h2>
 * <p>Registries store and manage plugin components (items, effects, handlers, etc.).
 * Access them via {@link fr.traqueur.items.api.registries.Registry#get(Class)}:</p>
 * <pre>{@code
 * ItemsRegistry items = Registry.get(ItemsRegistry.class);
 * EffectsRegistry effects = Registry.get(EffectsRegistry.class);
 * HandlersRegistry handlers = Registry.get(HandlersRegistry.class);
 * }</pre>
 *
 * <h2>Third-Party Integrations</h2>
 * <p><b>Hard Dependencies:</b></p>
 * <ul>
 *   <li><b>zMenu:</b> GUI framework for applicator menus ({@link #getInventoryManager()})</li>
 *   <li><b>RecipesAPI:</b> Advanced recipe system ({@link #getRecipesManager()})</li>
 * </ul>
 *
 * <p><b>Soft Dependencies (via Hooks):</b></p>
 * <ul>
 *   <li>Jobs, ZJobs - Job system integrations</li>
 *   <li>ItemsAdder, Nexo, Oraxen - Custom block providers</li>
 *   <li>WorldGuard, SuperiorSkyBlock2 - Location protection</li>
 *   <li>EconomyShopGUI, ShopGUIPlus, ZShop - Economy integrations</li>
 * </ul>
 *
 * <h2>External Plugin Usage</h2>
 * <p>Other plugins can integrate with zItems via managers:</p>
 * <pre>{@code
 * // In your plugin
 * public void onEnable() {
 *     Plugin zitemsPlugin = getServer().getPluginManager().getPlugin("zItems");
 *     if (zitemsPlugin == null || !zitemsPlugin.isEnabled()) {
 *         getLogger().warning("zItems not found!");
 *         return;
 *     }
 *
 *     // Access managers
 *     EffectsManager effectsManager = (EffectsManager) getServer()
 *         .getServicesManager()
 *         .load(EffectsManager.class);
 *
 *     ItemsManager itemsManager = (ItemsManager) getServer()
 *         .getServicesManager()
 *         .load(ItemsManager.class);
 *
 *     // Use the API
 *     effectsManager.applyEffect(player, item, effect);
 * }
 * }</pre>
 *
 * @see fr.traqueur.items.api.managers.Manager
 * @see fr.traqueur.items.api.registries.Registry
 * @see EffectsDispatcher
 */
public abstract class ItemsPlugin extends JavaPlugin {

    /**
     * Retrieves the RecipesAPI manager for advanced recipe handling.
     *
     * <p>The RecipesAPI is a third-party library (shaded into zItems) that provides
     * enhanced recipe functionality beyond vanilla Bukkit, including:</p>
     * <ul>
     *   <li>Complex shaped and shapeless crafting recipes</li>
     *   <li>Smithing table recipes (transform and trim)</li>
     *   <li>Furnace, blast furnace, smoker, and campfire recipes</li>
     *   <li>Stonecutter recipes</li>
     *   <li>Custom result conditions and transformations</li>
     * </ul>
     *
     * <p>This is used internally by {@link fr.traqueur.items.api.managers.ItemsManager#generateRecipesFromLoadedItems()}
     * to register recipes defined in {@link fr.traqueur.items.api.settings.RecipeSettings}.</p>
     *
     * @return the RecipesAPI instance managed by this plugin
     */
    public abstract RecipesAPI getRecipesManager();

    /**
     * Retrieves the {@link EffectsDispatcher} for event-driven effect execution.
     *
     * <p>The dispatcher is the core of the effect system. It receives Bukkit events
     * (e.g., {@code BlockBreakEvent}), extracts the relevant ItemStack, loads effects
     * from the item's PDC, finds applicable effect handlers, and executes them with
     * a shared {@link fr.traqueur.items.api.effects.EffectContext}.</p>
     *
     * <p><b>Typical Usage in Event Listeners:</b></p>
     * <pre>{@code
     * @EventHandler
     * public void onBlockBreak(BlockBreakEvent event) {
     *     // Dispatcher automatically handles this event
     *     // and executes applicable effects (Hammer, VeinMiner, etc.)
     * }
     * }</pre>
     *
     * <p>The dispatcher is also used to apply {@link fr.traqueur.items.api.effects.EffectHandler.NoEventEffectHandler}
     * effects during item creation:</p>
     * <pre>{@code
     * // Apply passive effects (attributes, enchantments)
     * dispatcher.applyNoEventEffects(player, itemStack, effects);
     * }</pre>
     *
     * @return the effects dispatcher instance
     * @see EffectsDispatcher
     * @see fr.traqueur.items.api.effects.EffectHandler
     */
    public abstract EffectsDispatcher getDispatcher();

    /**
     * Registers a manager with Bukkit's {@code ServicesManager}.
     *
     * <p>This method is called during plugin initialization to register manager
     * implementations as services. External plugins can then retrieve these services
     * to interact with zItems.</p>
     *
     * <p><b>Registration Example:</b></p>
     * <pre>{@code
     * // In plugin onEnable()
     * EffectsManager effectsManager = new ZEffectsManager(this);
     * registerManager(EffectsManager.class, effectsManager);
     *
     * ItemsManager itemsManager = new ZItemsManager(this);
     * registerManager(ItemsManager.class, itemsManager);
     * }</pre>
     *
     * <p><b>Service Priority:</b> Managers are registered with {@link ServicePriority#Normal}.
     * This allows other plugins to override the implementation if needed (though not recommended).</p>
     *
     * @param clazz   the manager interface class (e.g., {@code EffectsManager.class})
     * @param manager the concrete manager implementation instance
     * @param <I>     the manager type extending {@link Manager}
     * @return the registered manager instance (for method chaining)
     * @see Manager
     * @see org.bukkit.plugin.ServicesManager
     */
    public <I extends Manager> I registerManager(Class<I> clazz, I manager) {
        this.getServer().getServicesManager().register(clazz, manager, this, ServicePriority.Normal);
        return manager;
    }

    /**
     * Retrieves a registered manager instance from Bukkit's service system.
     *
     * <p>This is a convenience method for accessing managers within zItems code.
     * External plugins should use {@code Bukkit.getServicesManager().load()} instead.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * ItemsPlugin plugin = JavaPlugin.getPlugin(ItemsPlugin.class);
     * EffectsManager manager = plugin.getManager(EffectsManager.class);
     *
     * if (manager != null) {
     *     manager.applyEffect(player, item, effect);
     * }
     * }</pre>
     *
     * @param clazz the manager interface class to retrieve
     * @param <I>   the manager type extending {@link Manager}
     * @return the manager instance, or {@code null} if not registered
     * @see #registerManager(Class, Manager)
     */
    public <I extends Manager> I getManager(Class<I> clazz) {
        var rsp = this.getServer().getServicesManager().getRegistration(clazz);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }

    /**
     * Retrieves the zMenu {@link InventoryManager} for GUI-based interactions.
     *
     * <p>zMenu is a required dependency that provides a powerful GUI framework.
     * zItems uses it for:</p>
     * <ul>
     *   <li><b>Applicator GUI:</b> Interactive effect application interface</li>
     *   <li><b>Effect selection menus:</b> Browsing available effects</li>
     *   <li><b>Admin interfaces:</b> Management and debugging tools</li>
     * </ul>
     *
     * <p>Menu configurations are stored in the zMenu plugin directory
     * (typically {@code plugins/zMenu/inventories/}).</p>
     *
     * <p><b>Important:</b> This method assumes zMenu is installed and enabled.
     * The plugin will not function without zMenu.</p>
     *
     * @return the zMenu InventoryManager instance
     * @see <a href="https://github.com/Maxlego08/zMenu">zMenu on GitHub</a>
     */
    public abstract InventoryManager getInventoryManager();
}
