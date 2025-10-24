package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.CommandBlock;

/**
 * BlockData metadata for command block blocks.
 * Sets whether the command block is conditional.
 */
@AutoBlockDataMeta("command-block")
public record CommandBlockMeta(boolean conditional) implements BlockDataMeta<CommandBlock> {

    @Override
    public void apply(CommandBlock blockData) {
        blockData.setConditional(conditional);
    }
}
