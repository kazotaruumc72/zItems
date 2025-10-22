package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.annotations.EffectMeta;
import fr.traqueur.items.api.effects.EffectContext;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.shop.ShopProvider;
import fr.traqueur.items.effects.settings.AutoSellSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EffectMeta(value = "AUTO_SELL")
public record AutoSell(ItemsPlugin plugin) implements EffectHandler.MultiEventEffectHandler<AutoSellSettings> {

    @Override
    public Set<Class<? extends Event>> eventTypes() {
        return Set.of(BlockBreakEvent.class, EntityDeathEvent.class);
    }

    @Override
    public void handle(EffectContext context, AutoSellSettings settings) {
        Player player = context.executor();

        // Récupérer le ShopProvider
        ShopProvider provider;
        try {
            provider = ShopProvider.get();
        } catch (IllegalStateException e) {
            return; // Pas de provider disponible
        }

        // Liste des drops à garder (ceux qui n'ont pas été vendus)
        List<ItemStack> remainingDrops = new ArrayList<>();

        // Vendre chaque drop
        for (ItemStack drop : context.drops()) {
            if (drop == null || drop.getType().isAir()) {
                continue;
            }

            boolean sold = provider.sell(plugin, drop, drop.getAmount(), settings.multiplier(), player);

            // Si l'item n'a pas été vendu, on le garde
            if (!sold) {
                remainingDrops.add(drop);
            }
        }

        // Remplacer la liste des drops par ceux qui n'ont pas été vendus
        context.drops().clear();
        context.drops().addAll(remainingDrops);
    }

    @Override
    public int priority() {
        return -1;
    }

    @Override
    public Class<AutoSellSettings> settingsType() {
        return AutoSellSettings.class;
    }
}