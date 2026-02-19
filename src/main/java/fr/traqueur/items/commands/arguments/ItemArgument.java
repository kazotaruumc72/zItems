package fr.traqueur.items.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabCompleter;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ItemArgument implements ArgumentConverter<Item>, TabCompleter<CommandSender> {
    @Override
    public Item apply(String s) {
        return Registry.get(ItemsRegistry.class).getById(s);
    }

    @Override
    public List<String> onCompletion(CommandSender sender, List<String> args) {
        return Registry.get(ItemsRegistry.class).getAll().stream().map(Item::id).toList();
    }
}