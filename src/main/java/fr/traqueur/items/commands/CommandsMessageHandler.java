package fr.traqueur.items.commands;

import fr.traqueur.commands.api.logging.MessageHandler;
import fr.traqueur.items.Messages;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CommandsMessageHandler implements MessageHandler {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    @Override
    public String getNoPermissionMessage() {
        return SERIALIZER.serialize(Messages.NO_PERMISSION.get());
    }

    @Override
    public String getOnlyInGameMessage() {
        return SERIALIZER.serialize(Messages.ONLY_IN_GAME.get());
    }

    @Override
    public String getArgNotRecognized() {
        return SERIALIZER.serialize(Messages.ARG_NOT_RECOGNIZED.get());
    }

    @Override
    public String getRequirementMessage() {
        return SERIALIZER.serialize(Messages.REQUIREMENT_NOT_MET.get());
    }
}
