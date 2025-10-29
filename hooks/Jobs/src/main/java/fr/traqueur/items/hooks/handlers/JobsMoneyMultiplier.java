package fr.traqueur.items.hooks.handlers;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import fr.traqueur.items.hooks.jobs.JobsHandler;

public class JobsMoneyMultiplier extends JobsHandler<JobsPrePaymentEvent> {
    @Override
    public Class<JobsPrePaymentEvent> eventType() {
        return JobsPrePaymentEvent.class;
    }


    @Override
    protected void setNewValue(JobsPrePaymentEvent event, double boost) {
        event.setAmount(event.getAmount() * boost);
    }
}
