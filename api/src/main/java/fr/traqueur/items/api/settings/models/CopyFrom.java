package fr.traqueur.items.api.settings.models;

import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.api.Loadable;

/**
 * Configuration delegate for creating ItemStacks from external plugin sources.
 *
 * <p>This allows zItems to use items from other plugins as a base:
 * <ul>
 *   <li>{@code zitems} - Reference another zItems item</li>
 *   <li>{@code itemsadder} - Use an ItemsAdder custom item</li>
 *   <li>{@code nexo} - Use a Nexo custom item</li>
 *   <li>{@code oraxen} - Use an Oraxen custom item</li>
 * </ul>
 *
 * <p>Example YAML usage:
 * <pre>
 * copy-from:
 *   plugin-name: "itemsadder"
 *   item-id: "my_custom_sword"
 * </pre>
 *
 * @param pluginName the provider plugin name (e.g., "zitems", "itemsadder", "nexo", "oraxen")
 * @param itemId the item ID within that plugin's system
 */
public record CopyFrom(
        String pluginName,
        String itemId
) implements Loadable {
}
