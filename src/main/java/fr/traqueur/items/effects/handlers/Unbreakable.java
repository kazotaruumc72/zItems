package fr.traqueur.items.effects.handlers;

import fr.traqueur.items.api.annotations.AutoEffect;
import fr.traqueur.items.api.effects.EffectContext;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.effects.settings.EmptySettings;

@AutoEffect(value = "UNBREAKABLE")
public class Unbreakable implements EffectHandler.NoEventEffectHandler<EmptySettings> {

    @Override
    public void handle(EffectContext context, EmptySettings settings) {
        context.itemSource().editMeta(meta -> {
            meta.setUnbreakable(true);
        });
    }

    @Override
    public int priority() {
        return 0;
    }
}
