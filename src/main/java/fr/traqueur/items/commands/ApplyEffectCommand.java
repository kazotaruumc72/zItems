package fr.traqueur.items.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.items.Messages;
import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectApplicationResult;
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

        EffectApplicationResult result = manager.applyEffect(player, item, effect);

        switch (result) {
            case SUCCESS -> Messages.EFFECT_APPLIED.send(
                    player,
                    Placeholder.parsed("effect", effect.id())
            );
            case ALREADY_PRESENT -> Messages.EFFECT_ALREADY_PRESENT.send(
                    player,
                    Placeholder.parsed("effect", effect.id())
            );
            case INCOMPATIBLE -> Messages.EFFECT_INCOMPATIBLE.send(
                    player,
                    Placeholder.parsed("effect", effect.id())
            );
            case NOT_ALLOWED -> Messages.EFFECT_NOT_ALLOWED.send(player);
            case DISABLED -> Messages.EFFECT_DISABLED.send(
                    player,
                    Placeholder.parsed("effect", effect.id())
            );
            case HANDLER_NOT_FOUND -> Messages.EFFECT_HANDLER_NOT_FOUND.send(
                    player,
                    Placeholder.parsed("effect", effect.type())
            );
        }
    }
}
