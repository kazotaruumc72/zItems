package fr.traqueur.items.zjobs.handlers;

import fr.maxlego08.jobs.api.event.events.JobMoneyGainEvent;
import fr.traqueur.items.hooks.jobs.JobsHandler;

public class ZJobsMoneyMultiplier extends JobsHandler<JobMoneyGainEvent> {

    @Override
    protected void setNewValue(JobMoneyGainEvent event, double boost) {
        event.setMoney(event.getMoney() * boost);
    }
}
