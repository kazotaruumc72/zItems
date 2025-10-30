package fr.traqueur.items.zjobs.handlers;

import fr.maxlego08.jobs.api.event.events.JobExpGainEvent;
import fr.traqueur.items.hooks.jobs.JobsHandler;


public class ZJobsExperienceMultiplier extends JobsHandler<JobExpGainEvent> {

    @Override
    protected void setNewValue(JobExpGainEvent event, double boost) {
        event.setExperience(event.getExperience() * boost);
    }
}
