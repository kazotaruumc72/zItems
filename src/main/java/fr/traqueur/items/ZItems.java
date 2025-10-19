package fr.traqueur.items;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.utils.MessageUtil;
import fr.traqueur.items.api.settings.PluginSettings;
import fr.traqueur.items.api.settings.Settings;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;
import org.bukkit.Bukkit;

import java.io.File;

public class ZItems extends ItemsPlugin {

    private static final String CONFIG_FILE = "config.yml";
    private static final String MESSAGES_FILE = "messages.yml";

    @Override
    public void onEnable() {

        long enableTime = System.currentTimeMillis();

        this.saveDefaultConfig();
        this.reloadConfig();

        PluginSettings settings = Settings.get(PluginSettings.class);
        Logger.init(this.getSLF4JLogger(), settings.debug());
        MessageUtil.init(this);

        Logger.info("<yellow>=== ENABLE START ===");
        Logger.info("<gray>Plugin Version V<red>{}", this.getDescription().getVersion());

        Logger.info("<yellow>=== ENABLE DONE <gray>(<gold>" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<gray>) <yellow>===");
    }

    @Override
    public void onDisable() {
        long disableTime = System.currentTimeMillis();
        Logger.info("<yellow>=== DISABLE START ===");
        Logger.info("<gray>Plugin Version V<red>{}", this.getDescription().getVersion());

        MessageUtil.close();

        Logger.info("<yellow>=== DISABLE DONE <gray>(<gold>" + Math.abs(disableTime - System.currentTimeMillis()) + "ms<gray>) <yellow>===");
    }

    @Override
    public void saveDefaultConfig() {
        this.saveIfNotExits(CONFIG_FILE);
        this.saveIfNotExits(MESSAGES_FILE);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.createSettings(CONFIG_FILE, PluginSettings.class);
        try {
            Structura.loadEnum(this.getDataPath().resolve(MESSAGES_FILE), Messages.class);
        } catch (StructuraException e) {
            this.getSLF4JLogger().error("Failed to load messages configuration.", e);
        }
    }

    private <T extends Settings> void createSettings(String path, Class<T> clazz) {
        File file = new File(this.getDataFolder(), path);
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + path + " does not exist.");
        }
        T instance = Structura.load(file, clazz);
        Settings.register(clazz, instance);
    }

    private void saveIfNotExits(String fileName) {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        File file = new File(this.getDataFolder(), fileName);
        if (!file.exists()) {
            this.saveResource(fileName, false);
        }
    }

}
