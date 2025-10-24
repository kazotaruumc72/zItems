package fr.traqueur.items.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.items.Messages;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.items.Item;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveItemCommand extends Command<@NotNull ItemsPlugin> {

    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public GiveItemCommand(ItemsPlugin plugin) {
        super(plugin, "give");
        this.setDescription("Give a custom item to a player");
        this.setPermission("items.command.give");
        this.addArgs("player", Player.class, "item", Item.class, "amount", Integer.class);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Player target = arguments.get("player");
        Item item = arguments.get("item");
        int amount = arguments.get("amount");

        // Validate amount
        if (amount <= 0) {
            Messages.ITEM_GIVE_INVALID_AMOUNT.send(sender);
            return;
        }

        // Build the item
        ItemStack itemStack = item.build(target, amount);

        // Give the item to the player
        target.getInventory().addItem(itemStack);

        // Send success messages
        Messages.ITEM_GIVEN.send(
                sender,
                Placeholder.parsed("player", target.getName()),
                Placeholder.component("item", item.representativeName()),
                Placeholder.parsed("amount", String.valueOf(amount))
        );

        if (!sender.equals(target)) {
            Messages.ITEM_RECEIVED.send(
                    target,
                    Placeholder.component("item", item.representativeName()),
                    Placeholder.parsed("amount", String.valueOf(amount))
            );
        }
    }
}