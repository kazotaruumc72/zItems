package fr.traqueur.items.hooks.handlers;

import fr.maxlego08.jobs.api.event.events.JobMoneyGainEvent;
import fr.traqueur.items.hooks.jobs.JobsHandler;

public class ZJobsMoneyMultiplier extends JobsHandler<JobMoneyGainEvent> {
    @Override
    public Class<JobMoneyGainEvent> eventType() {
        return JobMoneyGainEvent.class;
    }


    @Override
    protected void setNewValue(JobMoneyGainEvent event, double boost) {
        event.setMoney(event.getMoney() * boost);
    }
}
