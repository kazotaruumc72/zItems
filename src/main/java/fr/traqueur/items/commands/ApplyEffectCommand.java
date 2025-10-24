package fr.traqueur.items.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.items.Messages;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.managers.EffectsManager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ApplyEffectCommand extends Command<@NotNull ItemsPlugin> {

    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public ApplyEffectCommand(ItemsPlugin plugin) {
        super(plugin, "applyeffect");
        this.setDescription("Apply an effect to an item");
        this.setPermission("items.command.applyeffect");
        this.setGameOnly(true);
        this.addArgs("effect", Effect.class);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Player player = (Player) sender;
        Effect effect = arguments.get("effect");
        ItemStack item = player.getInventory().getItemInMainHand();
        EffectsManager manager = this.getPlugin().getManager(EffectsManager.class);
        manager.applyEffect(player, item, effect);
        Messages.EFFECT_APPLIED.send(
                player,
                Placeholder.parsed("effect", effect.id())
        );
    }
}
