package fr.traqueur.items.api.shop;

import fr.traqueur.items.api.ItemsPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public abstract class ShopProvider {

     private static ShopProvider INSTANCE;

     public static void register(ShopProvider provider) {
         if (INSTANCE != null) {
                throw new IllegalStateException("A ShopProvider has already been registered");
         }
         INSTANCE = provider;
     }

     public static ShopProvider get() {
         if (INSTANCE == null) {
                throw new IllegalStateException("No ShopProvider has been registered");
         }
         return INSTANCE;
     }

     public abstract boolean sell(ItemsPlugin plugin, ItemStack item, int amount, double multiplier, OfflinePlayer player);

}
