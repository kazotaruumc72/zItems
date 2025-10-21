package fr.traqueur.items;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectsProvider;
import fr.traqueur.items.api.registries.EffectsRegistry;
import fr.traqueur.items.api.registries.ExtractorsRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.api.utils.MessageUtil;
import fr.traqueur.items.api.settings.PluginSettings;
import fr.traqueur.items.api.settings.Settings;
import fr.traqueur.items.effects.EffectsDispatcher;
import fr.traqueur.items.registries.EffectsRegistryImpl;
import fr.traqueur.items.effects.EventsListener;
import fr.traqueur.items.effects.HandlersProvider;
import fr.traqueur.items.registries.ExtractorsRegistryImpl;
import fr.traqueur.items.effects.settings.readers.AttributeReader;
import fr.traqueur.items.effects.settings.readers.EnchantmentReader;
import fr.traqueur.items.effects.settings.readers.EquipmentSlotGroupReader;
import fr.traqueur.items.effects.settings.readers.TagReader;
import fr.traqueur.items.shop.ShopProviders;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;
import fr.traqueur.structura.registries.CustomReaderRegistry;
import fr.traqueur.structura.types.TypeToken;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class ZItems extends ItemsPlugin {

    private static final String CONFIG_FILE = "config.yml";
    private static final String MESSAGES_FILE = "messages.yml";

    private EffectsDispatcher dispatcher;

    @Override
    public void onEnable() {

        long enableTime = System.currentTimeMillis();

        this.saveDefaultConfig();
        this.injectReaders();

        PluginSettings settings = this.createSettings(CONFIG_FILE, PluginSettings.class);
        Logger.init(this.getSLF4JLogger(), settings.debug());

        Logger.info("<yellow>=== ENABLE START ===");
        Logger.info("<gray>Plugin Version V<red>{}", this.getDescription().getVersion());

        this.reloadConfig();

        if (!ShopProviders.initialize()) {
            Logger.severe("No shop provider found! Disabling plugin.");
            Logger.info("Available shop providers:");
            for (ShopProviders shopProviders: ShopProviders.values()) {
                Logger.info("- <gold>{}", shopProviders.pluginName());
            }
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Logger.info("Shop provider <green>{} <reset>has been found.", ShopProviders.FOUND_PROVIDER.pluginName());

        MessageUtil.initialize(this);

        // Initialize handlers provider (scans all @EffectMeta handlers)
        HandlersProvider.initialize(this);

        // Register and load effects from files
        Registry.register(EffectsRegistry.class, new EffectsRegistryImpl(this));
        Registry.get(EffectsRegistry.class).loadFromFolder(this.getDataPath().resolve("effects"));

        // Register and load extractors
        Registry.register(ExtractorsRegistry.class, new ExtractorsRegistryImpl());
        Registry.get(ExtractorsRegistry.class).registerDefaults();

        Logger.info("Setting up event dispatching system...");
        //TODO make it configurable
        this.dispatcher = new EffectsDispatcher((item) ->
                List.of(Registry.get(EffectsRegistry.class).getById("absorption_pickaxe"),
                        Registry.get(EffectsRegistry.class).getById("super_hammer"),
                        Registry.get(EffectsRegistry.class).getById("xp_boost_pickaxe")));
        EventsListener eventsListener = new EventsListener(this.dispatcher);
        eventsListener.registerDynamicListeners(this);
        Logger.info("<green>Event dispatching system initialized successfully!");

        Logger.info("<yellow>=== ENABLE DONE <gray>(<gold>" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<gray>) <yellow>===");
    }

    private void injectReaders() {
        CustomReaderRegistry.getInstance().register(EquipmentSlotGroup.class, new EquipmentSlotGroupReader());
        CustomReaderRegistry.getInstance().register(Attribute.class, new AttributeReader());
        CustomReaderRegistry.getInstance().register(Enchantment.class, new EnchantmentReader());
        CustomReaderRegistry.getInstance().register(new TypeToken<>() {}, new TagReader());
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

    private <T extends Settings> T createSettings(String path, Class<T> clazz) {
        File file = new File(this.getDataFolder(), path);
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + path + " does not exist.");
        }
        T instance = Structura.load(file, clazz);
        Settings.register(clazz, instance);
        return instance;
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
