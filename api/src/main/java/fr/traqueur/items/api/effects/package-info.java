/**
 * Effect system API for zItems.
 *
 * <p>This package provides the complete effect framework, including effect handlers,
 * the event dispatcher, effect context, and application system. Effects modify item
 * behavior by responding to events or applying passive modifications.</p>
 *
 * <h2>Core Components</h2>
 *
 * <h3>Effect Handlers</h3>
 * <ul>
 *   <li>{@link fr.traqueur.items.api.effects.EffectHandler} - Sealed interface for all handler types</li>
 *   <li>{@link fr.traqueur.items.api.effects.EffectHandler.SingleEventEffectHandler} - Handlers for one event type</li>
 *   <li>{@link fr.traqueur.items.api.effects.EffectHandler.MultiEventEffectHandler} - Handlers for multiple events</li>
 *   <li>{@link fr.traqueur.items.api.effects.EffectHandler.NoEventEffectHandler} - Passive effects (attributes, enchants)</li>
 * </ul>
 *
 * <h3>Dispatcher and Context</h3>
 * <ul>
 *   <li>{@link fr.traqueur.items.api.effects.EffectsDispatcher} - Event-driven effect execution pipeline</li>
 *   <li>{@link fr.traqueur.items.api.effects.EffectContext} - Shared state across handlers (blocks, drops, XP)</li>
 * </ul>
 *
 * <h3>Effect Definitions</h3>
 * <ul>
 *   <li>{@link fr.traqueur.items.api.effects.Effect} - Effect instance (ID + settings)</li>
 *   <li>{@link fr.traqueur.items.api.effects.EffectSettings} - Polymorphic configuration base class</li>
 *   <li>{@link fr.traqueur.items.api.effects.EffectRepresentation} - Visual/physical effect representation</li>
 * </ul>
 *
 * <h3>Application System</h3>
 * <ul>
 *   <li>{@link fr.traqueur.items.api.effects.Applicator} - Defines how effects are applied (smithing, GUI, etc.)</li>
 *   <li>{@link fr.traqueur.items.api.effects.EffectApplicationResult} - Result enum (SUCCESS, INCOMPATIBLE, etc.)</li>
 * </ul>
 *
 * <h2>Effect Execution Flow</h2>
 * <pre>
 * 1. Bukkit Event (e.g., BlockBreakEvent)
 *    ↓
 * 2. EffectsDispatcher receives event
 *    ↓
 * 3. ItemStack extracted from event
 *    ↓
 * 4. Effects loaded from item's PDC
 *    ↓
 * 5. EffectContext created (shared state)
 *    ↓
 * 6. Applicable handlers found and sorted by priority
 *    ↓
 * 7. Handlers execute sequentially
 *    ↓
 * 8. Final state applied (break blocks, drop items)
 * </pre>
 *
 * <h2>Creating Custom Effects</h2>
 * <pre>{@code
 * @AutoEffect("MY_EFFECT")
 * public class MyEffectHandler implements EffectHandler.SingleEventEffectHandler<MySettings, BlockBreakEvent> {
 *
 *     @Override
 *     public void handle(Player player, ItemStack item, BlockBreakEvent event,
 *                        MySettings settings, EffectContext context) {
 *         // Custom effect logic
 *         Block block = event.getBlock();
 *         context.addBlock(block);
 *         context.addDrops(block.getDrops(item));
 *     }
 *
 *     @Override
 *     public Class<MySettings> getSettingsClass() {
 *         return MySettings.class;
 *     }
 *
 *     public static class MySettings extends EffectSettings {
 *         private int radius;
 *         public int getRadius() { return radius; }
 *     }
 * }
 * }</pre>
 *
 * @see fr.traqueur.items.api.effects.EffectHandler
 * @see fr.traqueur.items.api.effects.EffectsDispatcher
 * @see fr.traqueur.items.api.registries.HandlersRegistry
 * @see fr.traqueur.items.api.registries.EffectsRegistry
 */
package fr.traqueur.items.api.effects;