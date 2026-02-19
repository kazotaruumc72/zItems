package fr.traqueur.items.hooks.economyshopgui;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.shop.ShopProvider;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.api.objects.SellPrice;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class EconomyShopGUIProvider extends ShopProvider {

    @Override
    public boolean sell(ItemsPlugin plugin, ItemStack item, int amount, double multiplier, OfflinePlayer player) {
        Optional<SellPrice> optional = EconomyShopGUIHook.getSellPrice(player, item);
        if (optional.isEmpty())
            return false;
        SellPrice price = optional.get();
        EconomyShopGUIHook.getEcon(price.getShopItem().getEcoType()).depositBalance(player, price.getPrice(price.getShopItem().getEcoType()) * amount * multiplier);
        return true;
    }
}
