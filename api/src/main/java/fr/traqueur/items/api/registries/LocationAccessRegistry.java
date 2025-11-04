package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.blocks.LocationAccess;

/**
 * Registry for managing {@link LocationAccess} implementations that control block interaction permissions.
 *
 * <p>This registry stores LocationAccess providers that integrate with protection plugins
 * (WorldGuard, SuperiorSkyblock2, etc.) to check if players can break or place blocks
 * at specific locations. This is critical for effect handlers like Hammer and VeinMiner
 * that break multiple blocks at once.</p>
 *
 * <h2>Purpose</h2>
 * <p>LocationAccess implementations prevent players from bypassing region protection
 * when using area-breaking effects. Without these checks, a player could use a Hammer
 * effect to break blocks in protected regions even if they don't have permission.</p>
 *
 * <h2>Registry Keys</h2>
 * <p>LocationAccess providers are registered with the name of their target plugin:</p>
 * <ul>
 *   <li>{@code "WorldGuard"} - WorldGuard region protection</li>
 *   <li>{@code "SuperiorSkyBlock2"} - SuperiorSkyblock island protection</li>
 *   <li>Custom protection plugins can register their own implementations</li>
 * </ul>
 *
 * <h2>Registration via Hooks</h2>
 * <p>LocationAccess implementations are typically registered by {@link fr.traqueur.items.api.hooks.Hook}
 * modules during their {@code onEnable()} phase:</p>
 * <pre>{@code
 * @AutoHook("WorldGuard")
 * public class WorldGuardHook implements Hook {
 *     @Override
 *     public void onEnable() {
 *         LocationAccessRegistry registry = Registry.get(LocationAccessRegistry.class);
 *         registry.register("WorldGuard", new WorldGuardLocationAccess());
 *     }
 * }
 * }</pre>
 *
 * <h2>Usage in Effect Handlers</h2>
 * <p>Effect handlers should check all registered LocationAccess providers before
 * breaking blocks:</p>
 * <pre>{@code
 * // In Hammer or VeinMiner effect handler
 * LocationAccessRegistry accessRegistry = Registry.get(LocationAccessRegistry.class);
 *
 * for (Block block : blocksToBreak) {
 *     // Check all registered protection plugins
 *     boolean canBreak = accessRegistry.values().stream()
 *         .allMatch(access -> access.canBreak(player, block.getLocation()));
 *
 *     if (canBreak) {
 *         // Safe to break this block
 *         context.addBlock(block);
 *     } else {
 *         // Skip protected blocks
 *         continue;
 *     }
 * }
 * }</pre>
 *
 * <h2>Implementation Example</h2>
 * <pre>{@code
 * public class MyProtectionLocationAccess implements LocationAccess {
 *
 *     @Override
 *     public boolean canBreak(Player player, Location location) {
 *         // Check with your protection plugin
 *         MyRegionManager regions = MyPlugin.getRegionManager();
 *         return regions.hasPermission(player, location, Permission.BREAK);
 *     }
 *
 *     @Override
 *     public boolean canPlace(Player player, Location location) {
 *         MyRegionManager regions = MyPlugin.getRegionManager();
 *         return regions.hasPermission(player, location, Permission.PLACE);
 *     }
 * }
 * }</pre>
 *
 * @see LocationAccess
 * @see fr.traqueur.items.api.effects.EffectHandler
 */
public interface LocationAccessRegistry extends Registry<String, LocationAccess> {
}
