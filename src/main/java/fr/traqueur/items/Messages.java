package fr.traqueur.items;

import fr.traqueur.items.utils.MessageUtil;
import fr.traqueur.structura.api.Loadable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

public enum Messages implements Loadable {

    NO_PERMISSION("<red>You do not have permission to execute this command."),
    ONLY_IN_GAME("<red>This command can only be executed in-game."),
    ARG_NOT_RECOGNIZED("<red>Argument not recognized."),
    REQUIREMENT_NOT_MET("<red>You do not meet the requirements to perform this command."),

    EFFECT_APPLIED("<green>Effect <yellow><effect></yellow> applied successfully."),;

    private final String rawMessage;

    Messages(String message) {
        this.rawMessage = message;
    }

    /**
     * Gets the message Component with no placeholders.
     *
     * @return the parsed Component
     */
    public Component get() {
        return MessageUtil.parseMessage(rawMessage);
    }

    /**
     * Gets the message Component with placeholders replaced.
     * <p>
     * Example usage:
     * <pre>{@code
     * Messages.EFFECT_APPLIED.get(
     *     Placeholder.parsed("effect", "super_hammer")
     * )
     * }</pre>
     *
     * @param placeholders the placeholders to replace
     * @return the parsed Component with replaced placeholders
     */
    public Component get(TagResolver... placeholders) {
        return MessageUtil.parseMessage(rawMessage, placeholders);
    }

    /**
     * Sends this message to a command sender with placeholders replaced.
     * <p>
     * Example usage:
     * <pre>{@code
     * Messages.EFFECT_APPLIED.send(
     *     player,
     *     Placeholder.parsed("effect", "super_hammer")
     * )
     * }</pre>
     *
     * @param sender the command sender
     * @param placeholders the placeholders to replace
     */
    public void send(CommandSender sender, TagResolver... placeholders) {
        MessageUtil.sendMessage(sender, get(placeholders));
    }
}
