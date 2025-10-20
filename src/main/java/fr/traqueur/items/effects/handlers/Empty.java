package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.effects.EffectContext;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.effects.EffectMeta;
import fr.traqueur.items.effects.settings.EmptySettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@EffectMeta(value = "EMPTY")
public class Empty implements EffectHandler.NoEventEffectHandler<EmptySettings> {

    @Override
    public void handle(EffectContext context, EmptySettings settings) {
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
