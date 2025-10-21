package fr.traqueur.items.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.items.api.ItemsPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ZItemsCommand extends Command<@NotNull ItemsPlugin> {
    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public ZItemsCommand(ItemsPlugin plugin) {
        super(plugin, "zitems");
        this.addAlias("zit", "zitem");
        this.setPermission("zitems.command.admin");
        this.setDescription("Main command for ZItems plugin.");

        this.addSubCommand(new ApplyEffectCommand(plugin));
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        sender.sendMessage("ZItems Plugin - Version " + this.getPlugin().getDescription().getVersion());
        sender.sendMessage("Use /zitems help for a list of commands.");
    }
}
