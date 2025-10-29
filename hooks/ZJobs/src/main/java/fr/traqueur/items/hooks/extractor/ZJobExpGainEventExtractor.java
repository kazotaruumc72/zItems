package fr.traqueur.items.hooks.extractor;

import fr.maxlego08.jobs.api.event.events.JobExpGainEvent;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.jetbrains.annotations.Nullable;

public class ZJobExpGainEventExtractor implements ItemSourceExtractor<JobExpGainEvent> {
    @Override
    public @Nullable ExtractionResult extract(JobExpGainEvent event) {
        return new ExtractionResult(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
    }
}
