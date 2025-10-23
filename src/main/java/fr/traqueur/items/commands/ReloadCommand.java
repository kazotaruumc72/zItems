package fr.traqueur.items.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.items.api.ItemsPlugin;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends Command<ItemsPlugin> {

    public ReloadCommand(ItemsPlugin plugin) {
        super(plugin, "reload");
        this.setPermission("items.command.reload");
        this.setDescription("Reloads the plugin");
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        this.getPlugin().reloadConfig();
        sender.sendMessage("§aItems plugin reloaded successfully.");
    }
}
