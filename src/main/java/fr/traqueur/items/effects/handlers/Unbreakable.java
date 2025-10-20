package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.effects.EffectMeta;
import fr.traqueur.items.effects.settings.EmptySettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@EffectMeta(value = "UNBREAKABLE")
public class Unbreakable implements EffectHandler.NoEventEffectHandler<EmptySettings> {
    @Override
    public void effect(Player source, ItemStack itemSource, EmptySettings settings) {
        itemSource.editMeta(meta -> {
            meta.setUnbreakable(true);
        });
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Class<EmptySettings> settingsType() {
        return EmptySettings.class;
    }
}
