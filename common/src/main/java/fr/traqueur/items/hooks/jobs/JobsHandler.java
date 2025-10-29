package fr.traqueur.items.hooks.jobs;

import fr.traqueur.items.api.effects.EffectContext;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.effects.settings.BoostSettings;
import org.bukkit.event.Event;

import java.util.concurrent.ThreadLocalRandom;

public abstract class JobsHandler<E extends Event> implements EffectHandler.SingleEventEffectHandler<BoostSettings, E> {

    @Override
    public Class<BoostSettings> settingsType() {
        return BoostSettings.class;
    }

    @Override
    public void handle(EffectContext context, BoostSettings settings) {
        if (settings.chanceToBoost() != -1 && ThreadLocalRandom.current().nextDouble(0, 100) > settings.chanceToBoost()) {
            return;
        }
        this.setNewValue(context.getEventAs(eventType()), settings.boost());
    }

    protected abstract void setNewValue(E event, double boost);

    @Override
    public int priority() {
        return -1;
    }
}
