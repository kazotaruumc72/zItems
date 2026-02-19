package fr.traqueur.items.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.items.Messages;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.items.api.registries.HandlersRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.serialization.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewEffectCommand extends Command<@NotNull ItemsPlugin> {

    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public ViewEffectCommand(ItemsPlugin plugin) {
        super(plugin, "effect.view");
        this.setDescription("View effects on held item");
        this.setPermission("items.command.effect.view");
        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if player is holding an item
        if (item.getType() == Material.AIR) {
            Messages.VIEW_NO_ITEM.send(player);
            return;
        }

        // Get effects from PDC
        List<Effect> effects = Keys.EFFECTS.get(
                item.getItemMeta().getPersistentDataContainer(),
                new ArrayList<>()
        );

        if (effects.isEmpty()) {
            Messages.VIEW_NO_EFFECTS.send(player);
            return;
        }

        // Send header
        Messages.VIEW_HEADER.send(player, Placeholder.parsed("count", String.valueOf(effects.size())));

        // Get registries
        EffectsRegistry effectsRegistry = Registry.get(EffectsRegistry.class);
        HandlersRegistry handlersRegistry = Registry.get(HandlersRegistry.class);

        // Display each effect
        for (Effect effect : effects) {
            Component displayName = effect.displayName();

            // Get handler info
            EffectHandler<?> handler = handlersRegistry.getById(effect.type());
            String handlerType = effect.type();
            int priority = handler != null ? handler.priority() : 0;

            // Send effect line
            Messages.VIEW_EFFECT_LINE.send(
                    player,
                    Placeholder.parsed("id", effect.id()),
                    Placeholder.component("display-name", displayName),
                    Placeholder.parsed("type", handlerType),
                    Placeholder.parsed("priority", String.valueOf(priority))
            );
        }

        // Send footer
        Messages.VIEW_FOOTER.send(player);
    }
}