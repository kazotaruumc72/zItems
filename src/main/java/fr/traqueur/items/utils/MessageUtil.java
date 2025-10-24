package fr.traqueur.items.utils;

import fr.traqueur.items.PlatformType;
import fr.traqueur.items.api.Logger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

/**
 * Utility class for sending messages using Adventure API.
 * Automatically detects Paper (native Adventure) vs Spigot (Adventure Platform wrapper).
 */
public class MessageUtil {

    /**
     * Legacy Minecraft color codes mapping to MiniMessage tags.
     */
    private static final Map<String, String> LEGACY_TO_MINIMESSAGE = Map.ofEntries(
            Map.entry("&0", "<black>"),
            Map.entry("&1", "<dark_blue>"),
            Map.entry("&2", "<dark_green>"),
            Map.entry("&3", "<dark_aqua>"),
            Map.entry("&4", "<dark_red>"),
            Map.entry("&5", "<dark_purple>"),
            Map.entry("&6", "<gold>"),
            Map.entry("&7", "<gray>"),
            Map.entry("&8", "<dark_gray>"),
            Map.entry("&9", "<blue>"),
            Map.entry("&a", "<green>"),
            Map.entry("&b", "<aqua>"),
            Map.entry("&c", "<red>"),
            Map.entry("&d", "<light_purple>"),
            Map.entry("&e", "<yellow>"),
            Map.entry("&f", "<white>"),
            Map.entry("&l", "<bold>"),
            Map.entry("&m", "<strikethrough>"),
            Map.entry("&n", "<underlined>"),
            Map.entry("&o", "<italic>"),
            Map.entry("&r", "<reset>")
    );
    private static BukkitAudiences bukkitAudiences;
    private static MiniMessage miniMessage;

    /**
     * Initializes the MessageUtil with the plugin instance.
     * Detects server type and sets up appropriate Adventure backend.
     *
     * @param plugin The plugin instance
     */
    public static void initialize(Plugin plugin) {
        PlatformType platformType = PlatformType.detect();
        miniMessage = MiniMessage.miniMessage();

        if (platformType == PlatformType.SPIGOT) {
            bukkitAudiences = BukkitAudiences.create(plugin);
            Logger.info("<yellow>Detected Spigot server - Using Adventure Platform wrapper");
        } else {
            Logger.info("<yellow>Detected Paper server - Using native Adventure API");
        }
    }

    /**
     * Closes the BukkitAudiences instance (Spigot only).
     * Should be called on plugin disable.
     */
    public static void close() {
        if (bukkitAudiences != null) {
            bukkitAudiences.close();
            bukkitAudiences = null;
        }
    }

    /**
     * Sends a Component message to a player.
     * Handles Paper (native) vs Spigot (wrapper) automatically.
     *
     * @param player    The player to send the message to
     * @param component The Component to send
     */
    public static void sendMessage(Player player, Component component) {
        if (PlatformType.isPaper()) {
            player.sendMessage(component);
        } else {
            Audience audience = bukkitAudiences.player(player);
            audience.sendMessage(component);
        }
    }

    /**
     * Sends a Component message to a command sender.
     * Handles Paper (native) vs Spigot (wrapper) automatically.
     *
     * @param sender    The command sender
     * @param component The Component to send
     */
    public static void sendMessage(CommandSender sender, Component component) {
        if (PlatformType.isPaper()) {
            sender.sendMessage(component);
        } else {
            Audience audience = bukkitAudiences.sender(sender);
            audience.sendMessage(component);
        }
    }

    /**
     * Broadcasts a Component message to all online players.
     * Handles Paper (native) vs Spigot (wrapper) automatically.
     *
     * @param component The Component to broadcast
     */
    public static void broadcast(Component component) {
        if (PlatformType.isPaper()) {
            Bukkit.broadcast(component);
        } else {
            Audience audience = bukkitAudiences.all();
            audience.sendMessage(component);
        }
    }

    /**
     * Sends a Component action bar message to a player.
     * Handles Paper (native) vs Spigot (wrapper) automatically.
     *
     * @param player    The player to send the action bar to
     * @param component The Component to send
     */
    public static void sendActionBar(Player player, Component component) {
        if (PlatformType.isPaper()) {
            player.sendActionBar(component);
        } else {
            Audience audience = bukkitAudiences.player(player);
            audience.sendActionBar(component);
        }
    }

    /**
     * Parses a message with MiniMessage tags and legacy color codes.
     * Converts legacy codes to MiniMessage format first, then parses.
     *
     * @param message The message to parse
     * @return The parsed Component
     */
    public static Component parseMessage(String message) {
        String converted = convertLegacyToMiniMessage(message);
        return miniMessage.deserialize(converted);
    }

    /**
     * Parses a message with MiniMessage tags, legacy color codes, and custom placeholders.
     * Converts legacy codes to MiniMessage format first, then parses with placeholder resolution.
     * <p>
     * Example usage:
     * <pre>{@code
     * import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
     *
     * Component message = MessageUtil.parseMessage(
     *     "<green>Player <player> earned <amount> coins!",
     *     Placeholder.parsed("player", playerName),
     *     Placeholder.parsed("amount", String.valueOf(coins))
     * );
     * }</pre>
     *
     * @param message      The message to parse
     * @param placeholders The TagResolvers for placeholder replacement
     * @return The parsed Component with placeholders replaced
     */
    public static Component parseMessage(String message, TagResolver... placeholders) {
        String converted = convertLegacyToMiniMessage(message);
        return miniMessage.deserialize(converted, placeholders);
    }

    /**
     * Converts legacy Minecraft color codes (&<char>) to MiniMessage tags.
     *
     * @param message The message with legacy color codes
     * @return The message with MiniMessage tags
     */
    private static String convertLegacyToMiniMessage(String message) {
        if (!message.contains("&")) {
            return message;
        }

        StringBuilder builder = new StringBuilder(message.length() + 20);
        int length = message.length();

        for (int i = 0; i < length; i++) {
            if (message.charAt(i) == '&' && i + 1 < length) {
                String code = "&" + message.charAt(i + 1);
                String replacement = LEGACY_TO_MINIMESSAGE.get(code);
                if (replacement != null) {
                    builder.append(replacement);
                    i++; // Skip next char
                    continue;
                }
            }
            builder.append(message.charAt(i));
        }

        return builder.toString();
    }

}