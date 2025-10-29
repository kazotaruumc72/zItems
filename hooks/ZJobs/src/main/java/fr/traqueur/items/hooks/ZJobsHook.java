package fr.traqueur.items.hooks;

import fr.maxlego08.jobs.api.event.events.JobExpGainEvent;
import fr.maxlego08.jobs.api.event.events.JobMoneyGainEvent;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.registries.ExtractorsRegistry;
import fr.traqueur.items.api.registries.HandlersRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.hooks.extractor.ZJobExpGainEventExtractor;
import fr.traqueur.items.hooks.extractor.ZJobMoneyGainEventExtractor;
import fr.traqueur.items.hooks.handlers.ZJobsExperienceMultiplier;
import fr.traqueur.items.hooks.handlers.ZJobsMoneyMultiplier;

public class ZJobsHook implements Hook {
    @Override
    public void onEnable() {
        HandlersRegistry handlersRegistry = Registry.get(HandlersRegistry.class);
        ExtractorsRegistry extractorsRegistry = Registry.get(ExtractorsRegistry.class);

        extractorsRegistry.register(JobExpGainEvent.class, new ZJobExpGainEventExtractor());
        extractorsRegistry.register(JobMoneyGainEvent.class, new ZJobMoneyGainEventExtractor());

        handlersRegistry.register("JOB_XP_BOOST", new ZJobsExperienceMultiplier());
        handlersRegistry.register("JOB_MONEY_BOOST", new ZJobsMoneyMultiplier());
    }
}
