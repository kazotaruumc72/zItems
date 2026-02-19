package fr.traqueur.items.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabCompleter;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.items.api.registries.Registry;
import org.bukkit.command.CommandSender;

import java.util.List;

public class EffectArgument implements ArgumentConverter<Effect>, TabCompleter<CommandSender> {
    @Override
    public Effect apply(String s) {
        return Registry.get(EffectsRegistry.class).getById(s);
    }

    @Override
    public List<String> onCompletion(CommandSender sender, List<String> args) {
        return Registry.get(EffectsRegistry.class).getAll().stream().map(Effect::id).toList();
    }
}
