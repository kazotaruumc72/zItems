package fr.traqueur.items.items.metadata;

import fr.traqueur.items.api.annotations.MetadataMeta;
import fr.traqueur.items.api.interactions.InteractionAction;
import fr.traqueur.items.api.items.ItemMetadata;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.annotations.defaults.DefaultLong;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Metadata for item commands.
 * Allows executing commands when a player interacts with an item.
 */
@MetadataMeta("commands")
public record CommandsMetadata(
        List<ItemCommand> commands,
        @Options(optional = true) @DefaultBool(false) boolean needConfirmation
) implements ItemMetadata {

    @Override
    public void apply(ItemStack itemStack, @Nullable Player player) {}



    public record ItemCommand(
            CommandSender sender,
            @Options(optional = true) InteractionAction action,
            @Options(optional = true) List<String> commands,
            @Options(optional = true) List<String> messages,
            @Options(optional = true) ItemDamage damage,
            @Options(optional = true) @DefaultLong(0) long cooldown
    ) implements Loadable {

        /**
         * Who will execute the command.
         */
        public enum CommandSender {
            PLAYER,
            CONSOLE
        }

        /**
         * Type of damage to apply to the item.
         */
        public enum DamageType {
            AMOUNT,
            DURABILITY
        }

        /**
         * Represents damage configuration for an item.
         */
        public record ItemDamage(
                DamageType type,
                int quantity
        ) implements Loadable {
        }
    }
}
