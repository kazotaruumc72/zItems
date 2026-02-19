package fr.traqueur.items.api.events;

import fr.traqueur.items.api.items.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a custom {@link Item} is being built into an {@link ItemStack}.
 *
 * <p>This event is triggered during the item creation process, after the base ItemStack
 * has been constructed but before it's finalized. Listeners can modify the ItemStack
 * before it's given to the player or used in recipes.</p>
 *
 * <h2>Event Timing</h2>
 * <p>This event fires when:</p>
 * <ul>
 *   <li>A player receives a custom item via the {@code /zitems give} command</li>
 *   <li>A custom item is crafted through a recipe</li>
 *   <li>An effect representation item is created via {@link fr.traqueur.items.api.managers.EffectsManager#createEffectItem(fr.traqueur.items.api.effects.Effect, Player)}</li>
 *   <li>A custom item is spawned programmatically via {@link Item#build(Player, int)}</li>
 * </ul>
 *
 * <p><b>Important:</b> This event fires <b>before</b> NoEventEffects are applied.
 * The ItemStack will not yet have attributes or enchantments from effects.</p>
 *
 * <h2>Event Properties</h2>
 * <ul>
 *   <li><b>Cancellable:</b> No - This event cannot be cancelled</li>
 *   <li><b>Async:</b> No - Always fired on main thread</li>
 *   <li><b>Player Context:</b> Yes - Always associated with a player</li>
 * </ul>
 *
 * <h2>Use Cases</h2>
 * <pre>{@code
 * @EventHandler
 * public void onItemBuild(ItemBuildEvent event) {
 *     Item source = event.getSource();
 *     ItemStack item = event.getItemStack();
 *     Player player = event.getPlayer();
 *
 *     // Example 1: Add player-specific lore
 *     if (source.id().equals("personalized_sword")) {
 *         ItemMeta meta = item.getItemMeta();
 *         List<Component> lore = new ArrayList<>(meta.lore());
 *         lore.add(Component.text("Owner: " + player.getName()));
 *         meta.lore(lore);
 *         item.setItemMeta(meta);
 *         event.setItemStack(item);
 *     }
 *
 *     // Example 2: Track item creation statistics
 *     if (source.id().equals("legendary_item")) {
 *         plugin.getStatsTracker().recordItemCreation(player, source.id());
 *     }
 *
 *     // Example 3: Apply conditional modifications
 *     if (player.hasPermission("items.vip")) {
 *         // Add bonus enchantment for VIP players
 *         item.addEnchantment(Enchantment.UNBREAKING, 3);
 *         event.setItemStack(item);
 *     }
 * }
 * }</pre>
 *
 * <h2>Modification Guidelines</h2>
 * <ul>
 *   <li><b>Safe:</b> Adding lore, enchantments, or PDC data</li>
 *   <li><b>Safe:</b> Changing item amount via {@code ItemStack.setAmount()}</li>
 *   <li><b>Caution:</b> Changing material type (may break custom item detection)</li>
 *   <li><b>Caution:</b> Removing PDC keys (may prevent item from being recognized)</li>
 * </ul>
 *
 * @see Item#build(Player, int)
 * @see fr.traqueur.items.api.managers.ItemsManager
 */
public class ItemBuildEvent extends PlayerEvent {

    /** Handler list for the event */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Gets the handler list for this event.
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /** The source Item for which the ItemStack is being built */
    private final Item item;
    /** The ItemStack being built */
    private ItemStack itemStack;

    /**
     * Constructs a new ItemBuildEvent.
     *
     * @param who       the player for whom the item is being built
     * @param source    the source Item
     * @param itemStack the ItemStack being built
     */
    public ItemBuildEvent(Player who, Item source, ItemStack itemStack) {
        super(who);
        this.item = source;
        this.itemStack = itemStack;
    }

    /**
     * Gets the source Item for which the ItemStack is being built.
     *
     * @return the source Item
     */
    public Item getSource() {
        return item;
    }

    /**
     * Gets the ItemStack being built.
     *
     * @return the ItemStack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Sets the ItemStack being built.
     *
     * @param itemStack the new ItemStack
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Gets the handlers for this event.
     *
     * @return the handler list
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
