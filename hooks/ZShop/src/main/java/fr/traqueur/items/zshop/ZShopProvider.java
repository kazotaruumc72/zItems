package fr.traqueur.items.zshop;

import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.buttons.ItemButton;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.shop.ShopProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class ZShopProvider extends ShopProvider {

    @Override
    public boolean sell(ItemsPlugin plugin, ItemStack item, int amount, double multiplier, OfflinePlayer player) {
        if (!player.isOnline()) {
            return false;
        }

        var register = plugin.getServer().getServicesManager().getRegistration(ShopManager.class);
        if (register == null) {
            return false;
        }
        var shopManager = register.getProvider();
        for (ItemButton itemButton : shopManager.getItemButtons()) {
            if (!itemButton.canSell()) {
                continue;
            }

            if (itemButton.getItemStack().build(player.getPlayer(), false).isSimilar(item)) {
                double price = itemButton.getSellPrice(player.getPlayer(), amount);
                itemButton.getEconomy().depositMoney(player, price * multiplier, "Automatic sell");
                return true;
            }
        }
        return false;
    }
}
