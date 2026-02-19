package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.items.BlockDataMeta;
import org.bukkit.block.data.type.TrialSpawner;

/**
 * BlockData metadata for trial spawner blocks.
 * Sets the spawner state and ominous flag.
 */
@AutoBlockDataMeta("trial-spawner")
public record TrialSpawnerMeta(TrialSpawner.State state, boolean ominous) implements BlockDataMeta<TrialSpawner> {

    @Override
    public void apply(TrialSpawner blockData) {
        blockData.setTrialSpawnerState(state);
        blockData.setOminous(ominous);
    }
}
