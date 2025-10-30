package fr.traqueur.items.jobs.extractors;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.jetbrains.annotations.Nullable;

public class JobMoneyGainEventExtractor implements ItemSourceExtractor<JobsPrePaymentEvent> {
    @Override
    public @Nullable ExtractionResult extract(JobsPrePaymentEvent event) {
        return new ExtractionResult(event.getPlayer().getPlayer(), event.getPlayer().getPlayer().getInventory().getItemInMainHand());
    }
}
