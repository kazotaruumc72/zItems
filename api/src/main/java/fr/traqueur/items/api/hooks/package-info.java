/**
 * Third-party plugin integration system.
 *
 * <p>This package provides the hook framework for integrating zItems with other Bukkit/Spigot
 * plugins. Hooks are automatically discovered via annotations and loaded conditionally based
 * on plugin availability.</p>
 *
 * <h2>Hook System Overview</h2>
 * <p>Hooks enable zItems to:</p>
 * <ul>
 *   <li><b>Extract items from plugin events:</b> Handle events from Jobs, MMOItems, etc.</li>
 *   <li><b>Integrate custom block systems:</b> Detect ItemsAdder, Oraxen, Nexo blocks</li>
 *   <li><b>Respect region protection:</b> Check WorldGuard, SuperiorSkyblock permissions</li>
 *   <li><b>Connect to economy systems:</b> Enable AutoSell with shop plugins</li>
 * </ul>
 *
 * <h2>Core Components</h2>
 * <ul>
 *   <li>{@link fr.traqueur.items.api.hooks.Hook} - Base interface for all hooks</li>
 *   <li>{@link fr.traqueur.items.api.registries.HooksRegistry} - Hook discovery and management</li>
 * </ul>
 *
 * <h2>Creating a Hook</h2>
 * <pre>{@code
 * @AutoHook("MyPlugin")
 * public class MyPluginHook implements Hook {
 *
 *     @Override
 *     public void onEnable() {
 *         // Register extractors for plugin events
 *         ExtractorsRegistry extractors = Registry.get(ExtractorsRegistry.class);
 *         extractors.register(MyPluginEvent.class, new MyPluginExtractor());
 *
 *         // Register custom effect handlers
 *         HandlersRegistry handlers = Registry.get(HandlersRegistry.class);
 *         handlers.register("MY_EFFECT", new MyEffectHandler());
 *
 *         // Register location access provider
 *         LocationAccessRegistry locationAccess = Registry.get(LocationAccessRegistry.class);
 *         locationAccess.register("MyPlugin", new MyLocationAccess());
 *     }
 * }
 * }</pre>
 *
 * <h2>Hook Lifecycle</h2>
 * <ol>
 *   <li>zItems scans packages for {@code @AutoHook} annotations</li>
 *   <li>For each hook, checks if target plugin is loaded</li>
 *   <li>If plugin exists, instantiates hook class</li>
 *   <li>Registers hook in {@link fr.traqueur.items.api.registries.HooksRegistry}</li>
 *   <li>Calls {@link fr.traqueur.items.api.hooks.Hook#onEnable()}</li>
 * </ol>
 *
 * <h2>Built-in Hooks</h2>
 *
 * <h3>Job Systems</h3>
 * <ul>
 *   <li><b>Jobs</b> - Jobs Reborn integration</li>
 *   <li><b>ZJobs</b> - Custom jobs system</li>
 * </ul>
 *
 * <h3>Custom Block Providers</h3>
 * <ul>
 *   <li><b>ItemsAdder</b> - ItemsAdder custom blocks</li>
 *   <li><b>Oraxen</b> - Oraxen custom blocks</li>
 *   <li><b>Nexo</b> - Nexo custom blocks</li>
 * </ul>
 *
 * <h3>Protection Systems</h3>
 * <ul>
 *   <li><b>WorldGuard</b> - Region protection</li>
 *   <li><b>SuperiorSkyBlock2</b> - Island protection</li>
 * </ul>
 *
 * <h3>Economy/Shop Systems</h3>
 * <ul>
 *   <li><b>EconomyShopGUI</b> - Shop integration for AutoSell</li>
 *   <li><b>ShopGUIPlus</b> - Shop integration for AutoSell</li>
 *   <li><b>ZShop</b> - Custom shop integration</li>
 * </ul>
 *
 * <h2>Module Structure</h2>
 * <p>Hooks are organized in separate Gradle modules under {@code hooks/PluginName/}:</p>
 * <pre>
 * hooks/
 * ├── Jobs/
 * │   └── src/main/java/fr/traqueur/items/jobs/
 * │       ├── JobsHook.java
 * │       └── JobsExtractor.java
 * ├── WorldGuard/
 * │   └── src/main/java/fr/traqueur/items/worldguard/
 * │       ├── WorldGuardHook.java
 * │       └── WorldGuardLocationAccess.java
 * └── ...
 * </pre>
 *
 * <h2>Best Practices</h2>
 * <ul>
 *   <li>Use the plugin name exactly as returned by {@code Plugin.getName()}</li>
 *   <li>Check for plugin API compatibility in {@code onEnable()}</li>
 *   <li>Handle missing plugin classes gracefully</li>
 *   <li>Avoid hard dependencies - use reflection if needed</li>
 * </ul>
 *
 * @see fr.traqueur.items.api.hooks.Hook
 * @see fr.traqueur.items.api.registries.HooksRegistry
 * @see fr.traqueur.items.api.annotations.AutoHook
 */
package fr.traqueur.items.api.hooks;