package fr.traqueur.items.hooks.extractors;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.jetbrains.annotations.Nullable;

public class JobExpGainEventExtractor implements ItemSourceExtractor<JobsExpGainEvent> {
    @Override
    public @Nullable ExtractionResult extract(JobsExpGainEvent event) {
        return new ExtractionResult(event.getPlayer().getPlayer(), event.getPlayer().getPlayer().getInventory().getItemInMainHand());
    }
}
