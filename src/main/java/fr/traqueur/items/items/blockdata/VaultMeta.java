package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Vault;

/**
 * BlockData metadata for vault blocks.
 * Sets the vault state and ominous flag.
 */
public record VaultMeta(Vault.State state, boolean ominous) implements BlockDataMeta<Vault> {

    @Override
    public void apply(Vault blockData) {
        blockData.setTrialSpawnerState(state);
        blockData.setOminous(ominous);
    }
}
