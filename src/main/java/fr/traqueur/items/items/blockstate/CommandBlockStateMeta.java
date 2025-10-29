package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.structura.annotations.Options;
import net.kyori.adventure.text.Component;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;

/**
 * BlockState configuration for command blocks.
 * Allows setting command and name.
 */
@AutoBlockStateMeta("command-block")
public record CommandBlockStateMeta(
        @Options(optional = true) String command,
        @Options(optional = true) Component name
) implements BlockStateMeta<CommandBlock> {

    @Override
    public void apply(Player player, CommandBlock commandBlock) {
        if (command != null && !command.isEmpty()) {
            commandBlock.setCommand(command);
        }

        if (name != null) {
            commandBlock.name(name);
        }
    }
}