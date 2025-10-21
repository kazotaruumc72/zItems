package fr.traqueur.items;

import fr.traqueur.items.api.utils.MessageUtil;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.command.CommandSender;

public enum Messages implements Loadable {

    NO_PERMISSION("<red>You do not have permission to execute this command."),
    ONLY_IN_GAME("<red>This command can only be executed in-game."),
    ARG_NOT_RECOGNIZED("<red>Argument not recognized."),
    REQUIREMENT_NOT_MET("<red>You do not meet the requirements to perform this command."),;

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String get() {
        return this.message;
    }

    public void send(CommandSender sender) {
        MessageUtil.sendMessage(sender, MessageUtil.parseMessage(this.message));
    }
}
