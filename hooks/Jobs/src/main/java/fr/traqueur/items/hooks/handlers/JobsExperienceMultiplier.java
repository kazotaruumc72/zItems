package fr.traqueur.items.hooks.handlers;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import fr.traqueur.items.hooks.jobs.JobsHandler;


public class JobsExperienceMultiplier extends JobsHandler<JobsExpGainEvent> {

    @Override
    protected void setNewValue(JobsExpGainEvent event, double boost) {
        event.setExp(event.getExp() * boost);
    }
}
