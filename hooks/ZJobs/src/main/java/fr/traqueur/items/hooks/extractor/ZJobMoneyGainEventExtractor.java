package fr.traqueur.items.hooks.extractor;

import fr.maxlego08.jobs.api.event.events.JobMoneyGainEvent;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.jetbrains.annotations.Nullable;

public class ZJobMoneyGainEventExtractor implements ItemSourceExtractor<JobMoneyGainEvent> {
    @Override
    public @Nullable ExtractionResult extract(JobMoneyGainEvent event) {
        return new ExtractionResult(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
    }
}
