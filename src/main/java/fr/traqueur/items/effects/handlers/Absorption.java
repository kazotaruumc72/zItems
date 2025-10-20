package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.effects.EffectContext;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.effects.EffectMeta;
import fr.traqueur.items.effects.settings.EmptySettings;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@EffectMeta(value = "ABSORPTION")
public class Absorption implements EffectHandler.SingleEventEffectHandler<EmptySettings, BlockBreakEvent> {
    @Override
    public Class<BlockBreakEvent> eventType() {
        return BlockBreakEvent.class;
    }

    @Override
    public void handle(EffectContext context, EmptySettings settings) {
        List<ItemStack> items = new ArrayList<>(context.drops());
        var notAdd = context.executor().getInventory().addItem(items.toArray(ItemStack[]::new));
        context.drops().clear();
        context.addDrops(notAdd.values());
    }

    @Override
    public int priority() {
        return -1;
    }

    @Override
    public Class<EmptySettings> settingsType() {
        return EmptySettings.class;
    }
}
