package fr.traqueur.items.api.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * Orchestrates the execution pipeline of effects.
 * <p>
 * The dispatcher is responsible for:
 * <ol>
 *   <li>Retrieving effects from an ItemStack via the EffectsProvider</li>
 *   <li>Creating a shared {@link EffectContext} for all effects</li>
 *   <li>Finding applicable handlers for each effect</li>
 *   <li>Sorting handlers by priority</li>
 *   <li>Executing handlers sequentially in a pipeline</li>
 * </ol>
 * <p>
 * Example pipeline execution:
 * <pre>
 * BlockBreakEvent triggered
 *   → ItemStack has Effects: [Hammer, XPBoost, AutoSell]
 *   → Create EffectContext (shared state)
 *   → Sort handlers by priority: [Hammer(1), XPBoost(0), AutoSell(-1)]
 *   → Execute:
 *       1. HammerHandler → context.affectedBlocks() = [27 blocks]
 *       2. XPBoostHandler → boosts XP for all affected blocks
 *       3. AutoSellHandler → sells all collected drops
 *   → Return final context
 * </pre>
 */
public interface EffectsDispatcher {

    /**
     * Applies all NoEventEffectHandlers to an ItemStack.
     * <p>
     * This method should be called when creating items, giving items to players,
     * or any time you want to ensure NoEvent effects (like attributes, enchantments,
     * unbreakable, etc.) are applied to an item.
     * <p>
     * This is a convenience method that calls {@link #dispatch(Player, ItemStack, Event)}
     * with {@code event = null}, which automatically filters to only execute NoEventHandlers
     * (since {@code canApply(null)} returns {@code true} only for NoEventHandlers).
     * <p>
     * Example usage:
     * <pre>{@code
     * ItemStack customSword = new ItemStack(Material.DIAMOND_SWORD);
     * // Add effects to PDC here
     *
     * EffectsDispatcher dispatcher = ...; // Get from your plugin
     * dispatcher.applyNoEventEffects(player, customSword);
     * player.getInventory().addItem(customSword);
     * }</pre>
     *
     * @param player     the player context (can be null for some effects)
     * @param itemSource the ItemStack to apply effects to
     * @return the EffectContext after all handlers have executed, or null if no effects were found
     */
    EffectContext applyNoEventEffects(Player player, ItemStack itemSource);

    void applyNoEventEffect(Player player, ItemStack itemSource, Effect effect);

    /**
     * Dispatches an event to all applicable effect handlers.
     * <p>
     * This method creates a shared context and executes all applicable handlers
     * in priority order. Each handler can modify the context, and subsequent
     * handlers see those modifications (pipeline pattern).
     * <p>
     * When {@code event} is {@code null}, only {@link EffectHandler.NoEventEffectHandler}s
     * will be executed (since {@code canApply(null)} returns {@code true} only for them).
     *
     * @param player     the player who triggered the event
     * @param itemSource the ItemStack that may contain effects
     * @param event      the Bukkit event that triggered this dispatch (or null for NoEventHandlers)
     * @return the EffectContext after all handlers have executed, or null if no effects were found
     */
    EffectContext dispatch(Player player, ItemStack itemSource, Event event);
}
