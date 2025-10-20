package fr.traqueur.items.shop;

import fr.traqueur.items.api.shop.ShopProvider;
import org.bukkit.Bukkit;

import java.util.Comparator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum ShopProviders {

    Z_SHOP("ZShop", ZShopProvider::new, 1),
    ECONOMY_SHOP_GUI("EconomyShopGUI", EconomyShopGUIProvider::new, 0),
    SHOP_GUI_PLUS("ShopGUIPlus", ShopGUIPlusProvider::new, 0)
    ;

    private final String pluginName;
    private final Supplier<? extends ShopProvider> supplier;
    private final int priority;

    ShopProviders(String pluginName, Supplier<? extends ShopProvider> supplier, int priority) {
        this.pluginName = pluginName;
        this.supplier = supplier;
        this.priority = priority;
    }

    public void initialize() {
        Stream.of(ShopProviders.values()).sorted(Comparator.comparingInt(sp -> sp.priority)).forEach(shopProvider -> {
            if(shopProvider.isEnable()) {
                ShopProvider.register(shopProvider.supplier.get());
            }
        });
    }

    private boolean isEnable() {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null;
    }



}
