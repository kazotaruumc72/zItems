package fr.traqueur.items.hooks;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.ExtractorsRegistry;
import fr.traqueur.items.api.registries.HandlersRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.hooks.extractors.JobExpGainEventExtractor;
import fr.traqueur.items.hooks.extractors.JobMoneyGainEventExtractor;
import fr.traqueur.items.hooks.handlers.JobsExperienceMultiplier;
import fr.traqueur.items.hooks.handlers.JobsMoneyMultiplier;

public class JobsHook implements Hook {
    @Override
    public void onEnable() {
        HandlersRegistry handlersRegistry = Registry.get(HandlersRegistry.class);
        ExtractorsRegistry extractorsRegistry = Registry.get(ExtractorsRegistry.class);

        extractorsRegistry.register(JobsExpGainEvent.class, new JobExpGainEventExtractor());
        extractorsRegistry.register(JobsPrePaymentEvent.class, new JobMoneyGainEventExtractor());

        handlersRegistry.register("JOB_XP_BOOST", new JobsExperienceMultiplier());
        handlersRegistry.register("JOB_MONEY_BOOST", new JobsMoneyMultiplier());
    }
}
