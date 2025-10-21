package fr.traqueur.items.commands;

import fr.traqueur.commands.api.logging.MessageHandler;
import fr.traqueur.items.Messages;
import fr.traqueur.items.utils.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CommandsMessageHandler implements MessageHandler {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    @Override
    public String getNoPermissionMessage() {
        return SERIALIZER.serialize(MessageUtil.parseMessage(Messages.NO_PERMISSION.get()));
    }

    @Override
    public String getOnlyInGameMessage() {
        return SERIALIZER.serialize(MessageUtil.parseMessage(Messages.ONLY_IN_GAME.get()));
    }

    @Override
    public String getArgNotRecognized() {
        return SERIALIZER.serialize(MessageUtil.parseMessage(Messages.ARG_NOT_RECOGNIZED.get()));
    }

    @Override
    public String getRequirementMessage() {
        return SERIALIZER.serialize(MessageUtil.parseMessage(Messages.REQUIREMENT_NOT_MET.get()));
    }
}
