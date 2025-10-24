package fr.traqueur.items.shop;


import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.shop.ShopProvider;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class ShopGUIPlusProvider extends ShopProvider {
    @Override
    public boolean sell(ItemsPlugin plugin, ItemStack item, int amount, double multiplier, OfflinePlayer player) {
        if (!player.isOnline()) {
            return false;
        }
        double price = ShopGuiPlusApi.getItemStackPriceSell(player.getPlayer(), item);
        if (price == -1) {
            return false;
        }

        double total = price * amount * multiplier;
        ShopGuiPlusApi.getItemStackShop(item).getEconomyProvider().deposit(player.getPlayer(), total);
        return true;
    }
}
