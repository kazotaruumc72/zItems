package fr.traqueur.items.registries;

import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.ItemSourceExtractor;
import fr.traqueur.items.api.registries.ExtractorsRegistry;
import fr.traqueur.items.effects.extractors.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for ItemSourceExtractors with hierarchical resolution.
 * <p>
 * This registry supports both specific extractors for individual event types
 * and generic extractors for event hierarchies. When an extractor is requested
 * for an event type, the registry will:
 * <ol>
 *   <li>Look for an exact match (e.g., PlayerInteractExtractor for PlayerInteractEvent)</li>
 *   <li>Walk up the class hierarchy to find a generic extractor (e.g., PlayerEventExtractor for PlayerEvent)</li>
 *   <li>Cache the result to avoid repeated reflection lookups</li>
 * </ol>
 * <p>
 * Example hierarchy resolution:
 * <pre>
 * PlayerInteractEvent → PlayerInteractExtractor (exact match)
 * PlayerMoveEvent → PlayerEventExtractor (parent match)
 * CustomPlayerEvent → PlayerEventExtractor (parent match)
 * </pre>
 */
public class ZExtractorsRegistry implements ExtractorsRegistry {

    private final Map<Class<? extends Event>, ItemSourceExtractor<?>> extractors;
    private final Map<Class<? extends Event>, ItemSourceExtractor<?>> cache;

    public ZExtractorsRegistry() {
        this.extractors = new HashMap<>();
        this.cache = new HashMap<>();
    }

    /**
     * Registers all default extractors.
     * Generic extractors are registered first, then specific ones override them.
     */
    @Override
    public void registerDefaults() {
        // Generic extractors (parent classes)
        register(PlayerEvent.class, new PlayerEventExtractor());
        register(EntityEvent.class, new EntityEventExtractor());

        // Specific extractors (override generic behavior)
        register(BlockBreakEvent.class, new BlockBreakExtractor());
        register(BlockPlaceEvent.class, new BlockPlaceExtractor());
        register(PlayerInteractEvent.class, new PlayerInteractExtractor());
        register(PlayerItemDamageEvent.class, new PlayerItemDamageExtractor());
        register(EntityDeathEvent.class, new EntityDeathExtractor());
        register(EntityDamageByEntityEvent.class, new EntityDamageByEntityExtractor());

        Logger.info("Registered <gold>{}<reset> ItemSourceExtractors with hierarchy support", extractors.size());
    }

    /**
     * Checks if an extractor exists for the given event type (exact or hierarchical).
     *
     * @param eventType the event class to check
     * @return true if an extractor can be resolved, false otherwise
     */
    @Override
    public boolean has(Class<? extends Event> eventType) {
        return getById(eventType) != null;
    }

    @Override
    public void register(Class<? extends Event> aClass, ItemSourceExtractor<?> item) {
        extractors.put(aClass, item);
        // Clear cache for this event type since we're changing the extractor
        cache.remove(aClass);
        Logger.debug("Registered ItemSourceExtractor for: <aqua>{}<reset>", aClass.getSimpleName());
    }

    @Override
    public ItemSourceExtractor<?> getById(Class<? extends Event> eventType) {
        // 1. Check cache first
        ItemSourceExtractor<?> cached = cache.get(eventType);
        if (cached != null) {
            return cached;
        }

        // 2. Look for exact match
        ItemSourceExtractor<?> extractor = extractors.get(eventType);
        if (extractor != null) {
            cache.put(eventType, extractor);
            return extractor;
        }

        // 3. Walk up the class hierarchy
        Class<?> currentClass = eventType.getSuperclass();
        while (currentClass != null && Event.class.isAssignableFrom(currentClass)) {
            extractor = extractors.get(currentClass);
            if (extractor != null) {
                Logger.debug("Using fallback extractor <yellow>{}<reset> for event <aqua>{}<reset>",
                        currentClass.getSimpleName(), eventType.getSimpleName());
                cache.put(eventType, extractor);
                return extractor;
            }
            currentClass = currentClass.getSuperclass();
        }

        // 4. No extractor found
        Logger.debug("No extractor found for event type: {}", eventType.getSimpleName());
        return null;
    }

    @Override
    public Collection<ItemSourceExtractor<?>> getAll() {
        return extractors.values();
    }
}