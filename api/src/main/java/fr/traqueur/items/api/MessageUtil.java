package fr.traqueur.items.api;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for sending messages using Adventure API.
 * Automatically detects Paper (native Adventure) vs Spigot (Adventure Platform wrapper).
 */
public class MessageUtil {

    private static ServerType serverType;
    private static BukkitAudiences bukkitAudiences;
    private static MiniMessage miniMessage;

    /**
     * Legacy Minecraft color codes mapping to MiniMessage tags.
     */
    private static final Map<String, String> LEGACY_TO_MINIMESSAGE = new HashMap<>();

    static {
        // Legacy color codes to MiniMessage tags
        LEGACY_TO_MINIMESSAGE.put("&0", "<black>");
        LEGACY_TO_MINIMESSAGE.put("&1", "<dark_blue>");
        LEGACY_TO_MINIMESSAGE.put("&2", "<dark_green>");
        LEGACY_TO_MINIMESSAGE.put("&3", "<dark_aqua>");
        LEGACY_TO_MINIMESSAGE.put("&4", "<dark_red>");
        LEGACY_TO_MINIMESSAGE.put("&5", "<dark_purple>");
        LEGACY_TO_MINIMESSAGE.put("&6", "<gold>");
        LEGACY_TO_MINIMESSAGE.put("&7", "<gray>");
        LEGACY_TO_MINIMESSAGE.put("&8", "<dark_gray>");
        LEGACY_TO_MINIMESSAGE.put("&9", "<blue>");
        LEGACY_TO_MINIMESSAGE.put("&a", "<green>");
        LEGACY_TO_MINIMESSAGE.put("&b", "<aqua>");
        LEGACY_TO_MINIMESSAGE.put("&c", "<red>");
        LEGACY_TO_MINIMESSAGE.put("&d", "<light_purple>");
        LEGACY_TO_MINIMESSAGE.put("&e", "<yellow>");
        LEGACY_TO_MINIMESSAGE.put("&f", "<white>");
        LEGACY_TO_MINIMESSAGE.put("&l", "<bold>");
        LEGACY_TO_MINIMESSAGE.put("&m", "<strikethrough>");
        LEGACY_TO_MINIMESSAGE.put("&n", "<underlined>");
        LEGACY_TO_MINIMESSAGE.put("&o", "<italic>");
        LEGACY_TO_MINIMESSAGE.put("&r", "<reset>");
    }

    /**
     * Initializes the MessageUtil with the plugin instance.
     * Detects server type and sets up appropriate Adventure backend.
     *
     * @param plugin The plugin instance
     */
    public static void init(Plugin plugin) {
        serverType = detectServerType();
        miniMessage = MiniMessage.miniMessage();

        if (serverType == ServerType.SPIGOT) {
            // For Spigot, we need BukkitAudiences wrapper
            bukkitAudiences = BukkitAudiences.create(plugin);
            Logger.info("<yellow>Detected Spigot server - Using Adventure Platform wrapper");
        } else {
            // For Paper, native Adventure is available
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
     * Sends a message to a player with MiniMessage and legacy color code support.
     *
     * @param player  The player to send the message to
     * @param message The message with MiniMessage tags or legacy codes
     */
    public static void sendMessage(Player player, String message) {
        Component component = parseMessage(message);

        if (serverType == ServerType.PAPER) {
            // Use Paper's native Adventure API
            player.sendMessage(component);
        } else {
            // Use BukkitAudiences wrapper for Spigot
            Audience audience = bukkitAudiences.player(player);
            audience.sendMessage(component);
        }
    }

    /**
     * Sends a message to a command sender with MiniMessage and legacy color code support.
     *
     * @param sender  The command sender
     * @param message The message with MiniMessage tags or legacy codes
     */
    public static void sendMessage(CommandSender sender, String message) {
        Component component = parseMessage(message);

        if (serverType == ServerType.PAPER) {
            // Use Paper's native Adventure API
            sender.sendMessage(component);
        } else {
            // Use BukkitAudiences wrapper for Spigot
            Audience audience = bukkitAudiences.sender(sender);
            audience.sendMessage(component);
        }
    }

    /**
     * Broadcasts a message to all online players.
     *
     * @param message The message with MiniMessage tags or legacy codes
     */
    public static void broadcast(String message) {
        Component component = parseMessage(message);

        if (serverType == ServerType.PAPER) {
            // Use Paper's native Adventure API
            Bukkit.broadcast(component);
        } else {
            // Use BukkitAudiences wrapper for Spigot
            Audience audience = bukkitAudiences.all();
            audience.sendMessage(component);
        }
    }

    /**
     * Sends an action bar message to a player.
     *
     * @param player  The player to send the action bar to
     * @param message The message with MiniMessage tags or legacy codes
     */
    public static void sendActionBar(Player player, String message) {
        Component component = parseMessage(message);

        if (serverType == ServerType.PAPER) {
            // Use Paper's native Adventure API
            player.sendActionBar(component);
        } else {
            // Use BukkitAudiences wrapper for Spigot
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
        // Convert legacy codes to MiniMessage tags
        String converted = convertLegacyToMiniMessage(message);
        // Parse with MiniMessage
        return miniMessage.deserialize(converted);
    }

    /**
     * Converts legacy Minecraft color codes (&<char>) to MiniMessage tags.
     *
     * @param message The message with legacy color codes
     * @return The message with MiniMessage tags
     */
    private static String convertLegacyToMiniMessage(String message) {
        String converted = message;
        for (Map.Entry<String, String> entry : LEGACY_TO_MINIMESSAGE.entrySet()) {
            converted = converted.replace(entry.getKey(), entry.getValue());
        }
        return converted;
    }

    /**
     * Detects the server type (Paper or Spigot).
     *
     * @return The detected server type
     */
    private static ServerType detectServerType() {
        try {
            // Try to load a Paper-specific class
            Class.forName("io.papermc.paper.text.PaperComponents");
            return ServerType.PAPER;
        } catch (ClassNotFoundException e) {
            return ServerType.SPIGOT;
        }
    }

    /**
     * Gets the current server type.
     *
     * @return The server type (PAPER or SPIGOT)
     */
    public static ServerType getServerType() {
        return serverType;
    }

    /**
     * Enum representing the server type.
     */
    public enum ServerType {
        PAPER,
        SPIGOT
    }
}