package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * BlockState configuration for spawner blocks.
 * Allows setting spawner properties like entity type, spawn delay, and range.
 */
@AutoBlockStateMeta("spawner")
public record SpawnerStateMeta(
        @Options(optional = true) EntityType spawnedType,
        @Options(optional = true) @DefaultInt(-1) int delay,
        @Options(optional = true) @DefaultInt(-1) int minSpawnDelay,
        @Options(optional = true) @DefaultInt(-1) int maxSpawnDelay,
        @Options(optional = true) @DefaultInt(-1) int spawnCount,
        @Options(optional = true) @DefaultInt(-1) int maxNearbyEntities,
        @Options(optional = true) @DefaultInt(-1) int requiredPlayerRange,
        @Options(optional = true) @DefaultInt(-1) int spawnRange
) implements BlockStateMeta<CreatureSpawner> {

    @Override
    public void apply(Player player, CreatureSpawner spawner) {
        if (spawnedType != null) {
            spawner.setSpawnedType(spawnedType);
        }

        if (delay >= 0) {
            spawner.setDelay(delay);
        }

        if (minSpawnDelay >= 0) {
            spawner.setMinSpawnDelay(minSpawnDelay);
        }

        if (maxSpawnDelay >= 0) {
            spawner.setMaxSpawnDelay(maxSpawnDelay);
        }

        if (spawnCount >= 0) {
            spawner.setSpawnCount(spawnCount);
        }

        if (maxNearbyEntities >= 0) {
            spawner.setMaxNearbyEntities(maxNearbyEntities);
        }

        if (requiredPlayerRange >= 0) {
            spawner.setRequiredPlayerRange(requiredPlayerRange);
        }

        if (spawnRange >= 0) {
            spawner.setSpawnRange(spawnRange);
        }
    }
}