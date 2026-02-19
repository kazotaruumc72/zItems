package fr.traqueur.items.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a spawner is about to be dropped after being broken with a Silk Touch tool.
 *
 * <p>This event is part of the SilkSpawner effect handler, which allows players to pick up
 * spawners using tools with Silk Touch enchantment. The event fires before the spawner
 * is actually dropped, allowing modifications to the dropped ItemStack or cancellation.</p>
 *
 * <h2>Event Timing</h2>
 * <p>This event fires when:</p>
 * <ul>
 *   <li>A player breaks a spawner block with a Silk Touch tool</li>
 *   <li>The player's tool has the SilkSpawner effect applied</li>
 *   <li>The spawner is about to be converted to an ItemStack drop</li>
 * </ul>
 *
 * <p>The event fires <b>before</b> the spawner block is removed from the world and
 * <b>before</b> the ItemStack is dropped at the location.</p>
 *
 * <h2>Event Properties</h2>
 * <ul>
 *   <li><b>Cancellable:</b> Yes - Prevents the spawner from dropping</li>
 *   <li><b>Async:</b> No - Always fired on main thread</li>
 *   <li><b>Player Context:</b> Yes - The player who broke the spawner</li>
 * </ul>
 *
 * <h2>Use Cases</h2>
 * <pre>{@code
 * @EventHandler
 * public void onSpawnerDrop(SpawnerDropEvent event) {
 *     Player player = event.getPlayer();
 *     ItemStack spawner = event.getItemStack();
 *     Location location = event.getLocation();
 *
 *     // Example 1: Permission-based restriction
 *     if (!player.hasPermission("items.silkspawner")) {
 *         event.setCancelled(true);
 *         player.sendMessage("You don't have permission to pick up spawners!");
 *         return;
 *     }
 *
 *     // Example 2: Region-based protection
 *     if (isInProtectedRegion(location)) {
 *         event.setCancelled(true);
 *         player.sendMessage("Cannot pick up spawners in this region!");
 *         return;
 *     }
 *
 *     // Example 3: Modify spawner lore with player info
 *     ItemMeta meta = spawner.getItemMeta();
 *     List<Component> lore = new ArrayList<>();
 *     lore.add(Component.text("Collected by: " + player.getName()));
 *     lore.add(Component.text("Location: " + location.getBlockX() + ", " +
 *                            location.getBlockY() + ", " + location.getBlockZ()));
 *     meta.lore(lore);
 *     spawner.setItemMeta(meta);
 *     event.setItemStack(spawner);
 *
 *     // Example 4: Track spawner collection statistics
 *     CreatureSpawner spawnerState = (CreatureSpawner) location.getBlock().getState();
 *     EntityType type = spawnerState.getSpawnedType();
 *     plugin.getStatsTracker().recordSpawnerCollection(player, type);
 *
 *     // Example 5: Custom drop handling (direct inventory)
 *     if (player.hasPermission("items.silkspawner.direct")) {
 *         event.setCancelled(true);
 *         player.getInventory().addItem(spawner);
 *         player.sendMessage("Spawner added to inventory!");
 *     }
 * }
 * }</pre>
 *
 * <h2>Cancellation Behavior</h2>
 * <p>When this event is cancelled:</p>
 * <ul>
 *   <li>The spawner ItemStack will <b>not</b> be dropped</li>
 *   <li>The spawner block is <b>still removed</b> from the world</li>
 *   <li>No natural drops occur (unlike normal spawner breaking)</li>
 *   <li>Listeners should handle custom drop logic if cancelling</li>
 * </ul>
 *
 * <h2>ItemStack Modifications</h2>
 * <p>The ItemStack can be safely modified before dropping:</p>
 * <ul>
 *   <li><b>Lore:</b> Add custom lore lines (player name, coordinates, etc.)</li>
 *   <li><b>Display Name:</b> Customize the spawner's display name</li>
 *   <li><b>Amount:</b> Change drop quantity (default is 1)</li>
 *   <li><b>PDC Data:</b> Add tracking or authentication data</li>
 * </ul>
 *
 * <p><b>Warning:</b> Changing the material type may cause the spawner to lose
 * its spawned entity type. The BlockStateMeta should be preserved.</p>
 *
 * @see fr.traqueur.items.api.effects.EffectHandler
 */
public class SpawnerDropEvent extends PlayerEvent implements Cancellable {

    /** Handler list for the event */
    private static final HandlerList HANDLERS = new HandlerList();

    /** Location where the spawner is dropped */
    private final Location location;
    /** ItemStack representing the dropped spawner */
    private ItemStack itemStack;
    /** Cancellation status of the event */
    private boolean cancelled;

    /**
     * Constructs a new SpawnerDropEvent.
     *
     * @param who       The player who caused the event
     * @param location  The location where the spawner is dropped
     * @param itemStack The ItemStack representing the dropped spawner
     */
    public SpawnerDropEvent(@NotNull Player who, Location location, ItemStack itemStack) {
        super(who);
        this.location = location;
        this.itemStack = itemStack;
        cancelled = false;
    }

    /**
     * Gets the handler list for the event.
     *
     * @return The handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    /**
     * Gets the location where the spawner is dropped.
     *
     * @return The drop location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the ItemStack representing the dropped spawner.
     *
     * @return The ItemStack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Sets the ItemStack representing the dropped spawner.
     *
     * @param itemStack The new ItemStack
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
