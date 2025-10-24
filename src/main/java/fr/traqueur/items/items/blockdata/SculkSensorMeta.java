package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.SculkSensor;

/**
 * BlockData metadata for sculk sensor blocks.
 * Sets the sensor phase.
 */
@BlockDataMetaMeta("sculk-sensor")
public record SculkSensorMeta(SculkSensor.Phase phase) implements BlockDataMeta<SculkSensor> {

    @Override
    public void apply(SculkSensor blockData) {
        blockData.setPhase(phase);
    }
}
