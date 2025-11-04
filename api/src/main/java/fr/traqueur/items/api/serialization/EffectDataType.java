package fr.traqueur.items.api.serialization;

import fr.traqueur.items.api.effects.Effect;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Custom {@link PersistentDataType} for serializing {@link Effect} objects to/from item PDC.
 *
 * <p>This class enables storing effect data directly in ItemStack metadata using Bukkit's
 * {@link org.bukkit.persistence.PersistentDataContainer} system. Effects are serialized
 * as strings (typically JSON) for persistence across server restarts.</p>
 *
 * <h2>Singleton Pattern</h2>
 * <p>This class follows the singleton pattern with a static {@link #INSTANCE} field that
 * is initialized by the plugin implementation during startup. The constructor is protected
 * to prevent external instantiation.</p>
 *
 * <h2>Usage in PDC</h2>
 * <pre>{@code
 * // Store an effect in an ItemStack
 * ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
 * ItemMeta meta = item.getItemMeta();
 * PersistentDataContainer pdc = meta.getPersistentDataContainer();
 *
 * Effect effect = ...; // Get effect from registry
 * NamespacedKey key = new NamespacedKey(plugin, "custom_effect");
 * pdc.set(key, EffectDataType.INSTANCE, effect);
 *
 * item.setItemMeta(meta);
 *
 * // Retrieve the effect later
 * if (pdc.has(key, EffectDataType.INSTANCE)) {
 *     Effect storedEffect = pdc.get(key, EffectDataType.INSTANCE);
 * }
 * }</pre>
 *
 * <h2>Effect Storage Format</h2>
 * <p>Effects are typically stored as JSON strings containing:</p>
 * <ul>
 *   <li>Effect ID (references {@link fr.traqueur.items.api.registries.EffectsRegistry})</li>
 *   <li>Effect settings (if customized per-item)</li>
 *   <li>Application timestamp (optional)</li>
 * </ul>
 *
 * <h2>Implementation Requirements</h2>
 * <p>Concrete implementations must:</p>
 * <ol>
 *   <li>Implement methods to serialize/deserialize Effect objects</li>
 *   <li>Initialize the {@link #INSTANCE} field during plugin startup</li>
 *   <li>Handle missing effects gracefully (e.g., after config removal)</li>
 * </ol>
 *
 * @see Effect
 * @see TrackedBlockDataType
 * @see org.bukkit.persistence.PersistentDataType
 */
public abstract class EffectDataType implements PersistentDataType<String, Effect> {

    /**
     * Singleton instance of the EffectDataType implementation.
     *
     * <p>This field is initialized by the plugin implementation during startup.
     * It must be accessed after the plugin has loaded, typically during or after
     * {@code onEnable()}.</p>
     *
     * <p><b>Warning:</b> Accessing this field before initialization will result in
     * {@code NullPointerException}. Always ensure the plugin is loaded before using.</p>
     */
    public static EffectDataType INSTANCE;

    /**
     * Protected constructor to enforce singleton pattern.
     *
     * <p>Only the plugin implementation should instantiate this class and assign
     * the instance to {@link #INSTANCE}.</p>
     */
    protected EffectDataType() {}

    /**
     * Returns the primitive type used for storage (String).
     *
     * <p>Effects are serialized to strings (typically JSON) for compact storage
     * in the PersistentDataContainer.</p>
     *
     * @return {@code String.class}
     */
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    /**
     * Returns the complex type being serialized (Effect).
     *
     * @return {@code Effect.class}
     */
    @Override
    public @NotNull Class<Effect> getComplexType() {
        return Effect.class;
    }
}
