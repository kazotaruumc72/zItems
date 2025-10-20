package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.effects.settings.EmptySettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Empty implements EffectHandler.NoEventEffectHandler<EmptySettings> {
    @Override
    public void effect(Player source, ItemStack itemSource, EmptySettings settings) {
        // No effect
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
