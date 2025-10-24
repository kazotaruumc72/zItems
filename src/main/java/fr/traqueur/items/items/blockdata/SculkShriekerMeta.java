package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.BlockDataMetaMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.SculkShrieker;

/**
 * BlockData metadata for sculk shrieker blocks.
 * Sets shrieking state and whether it can summon.
 */
@BlockDataMetaMeta("sculk-shrieker")
public record SculkShriekerMeta(boolean shrieking, boolean canSummon) implements BlockDataMeta<SculkShrieker> {

    @Override
    public void apply(SculkShrieker blockData) {
        blockData.setShrieking(shrieking);
        blockData.setCanSummon(canSummon);
    }
}
