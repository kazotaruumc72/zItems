package fr.traqueur.items.api.annotations;

import fr.traqueur.items.api.effects.ItemSourceExtractor;
import org.bukkit.event.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark an {@link ItemSourceExtractor} implementation for automatic registration.
 * <p>
 * The annotated class must implement {@link ItemSourceExtractor} and will be discovered
 * via package scanning by the {@link fr.traqueur.items.api.registries.ExtractorsRegistry}.
 * <p>
 * Example:
 * <pre>{@code
 * @ExtractorMeta(BlockBreakEvent.class)
 * public class BlockBreakExtractor implements ItemSourceExtractor<BlockBreakEvent> {
 *     @Override
 *     public ExtractionResult extract(BlockBreakEvent event) {
 *         Player player = event.getPlayer();
 *         ItemStack item = player.getInventory().getItemInMainHand();
 *         return new ExtractionResult(player, item);
 *     }
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoExtractor {
    /**
     * The event class this extractor handles.
     *
     * @return the event class
     */
    Class<? extends Event> value();
}