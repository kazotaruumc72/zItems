package fr.traqueur.items.api.shop;

import fr.traqueur.items.api.ItemsPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class representing a Shop Provider.
 * <p>
 * Implementations of this class should provide the logic for selling items.
 */
public abstract class ShopProvider {

    /** Singleton instance of ShopProvider */
    private static ShopProvider INSTANCE;

    /** Protected constructor to prevent direct instantiation */
    protected ShopProvider() {}

    /**
     * Registers a ShopProvider instance.
     *
     * @param provider The ShopProvider instance to register
     * @throws IllegalStateException if a ShopProvider has already been registered
     */
    public static void register(ShopProvider provider) {
        if (INSTANCE != null) {
            throw new IllegalStateException("A ShopProvider has already been registered");
        }
        INSTANCE = provider;
    }

    /**
     * Retrieves the registered ShopProvider instance.
     *
     * @return The registered ShopProvider instance
     * @throws IllegalStateException if no ShopProvider has been registered
     */
    public static @NotNull ShopProvider get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("No ShopProvider has been registered");
        }
        return INSTANCE;
    }

    /**
     * Sells a specified amount of an item for a player with a given multiplier.
     *
     * @param plugin     The ItemsPlugin instance
     * @param item       The ItemStack to sell
     * @param amount     The amount of the item to sell
     * @param multiplier The price multiplier
     * @param player     The OfflinePlayer who is selling the item
     * @return true if the sale was successful, false otherwise
     */
    public abstract boolean sell(ItemsPlugin plugin, ItemStack item, int amount, double multiplier, OfflinePlayer player);

}
