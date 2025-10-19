package fr.traqueur.items;

import fr.traqueur.items.api.MessageUtil;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.command.CommandSender;

public enum Messages implements Loadable {

    TEST_MESSAGE("<green>This is a test message!")
    ;

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public void send(CommandSender sender) {
        MessageUtil.sendMessage(sender, this.message);
    }
}
