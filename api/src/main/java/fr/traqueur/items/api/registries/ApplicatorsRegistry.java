package fr.traqueur.items.api.registries;

import fr.traqueur.items.api.effects.Applicator;
import fr.traqueur.items.api.effects.Effect;

/**
 * Registry for {@link Applicator} instances that handle effect application through various methods.
 *
 * <p>Applicators define <b>how</b> effects are applied to items, separate from the effect
 * logic itself. They can represent different application methods like smithing tables,
 * applicator GUIs, or custom mechanics.</p>
 *
 * <h2>Purpose</h2>
 * <p>The applicator system provides flexibility in how effects are applied:</p>
 * <ul>
 *   <li><b>Smithing Table:</b> Apply effects using vanilla smithing mechanics</li>
 *   <li><b>Applicator GUI:</b> Interactive menu-based effect application</li>
 *   <li><b>Custom Mechanics:</b> Plugin-specific application methods</li>
 *   <li><b>Recipe Generation:</b> Create recipes for effect application</li>
 * </ul>
 *
 * <h2>Registry Keys</h2>
 * <p>Applicators are registered using the effect ID as the key. Each effect can have
 * one applicator that defines how it's applied to items.</p>
 *
 * <h2>Applicator Types</h2>
 * <p>Common applicator implementations:</p>
 * <ul>
 *   <li><b>SmithingApplicator:</b> Uses smithing table with template/base/addition slots</li>
 *   <li><b>GUIApplicator:</b> Opens a custom zMenu interface for effect selection</li>
 *   <li><b>CommandApplicator:</b> Applies via commands only (no recipe)</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * // Get applicator for a specific effect
 * EffectsRegistry effects = Registry.get(EffectsRegistry.class);
 * Effect hammerEffect = effects.get("hammer").orElseThrow();
 *
 * ApplicatorsRegistry applicators = Registry.get(ApplicatorsRegistry.class);
 * Applicator applicator = applicators.getByEffect(hammerEffect);
 *
 * if (applicator != null) {
 *     // Check if effect can be applied via this applicator
 *     if (applicator.canApply(player, item, hammerEffect)) {
 *         // Generate recipe for smithing table
 *         applicator.generateRecipe(hammerEffect);
 *     }
 * }
 * }</pre>
 *
 * <h2>Registration</h2>
 * <p>Applicators are typically registered during plugin initialization:</p>
 * <pre>{@code
 * ApplicatorsRegistry registry = Registry.get(ApplicatorsRegistry.class);
 *
 * // Register smithing table applicator for an effect
 * Applicator smithingApplicator = new SmithingApplicator(
 *     templateItem,  // Template (e.g., netherite upgrade)
 *     baseItem,      // Base item (tool/armor to apply effect to)
 *     additionItem   // Addition (effect representation item)
 * );
 * registry.register("hammer", smithingApplicator);
 * }</pre>
 *
 * <h2>Relationship to Effects</h2>
 * <p>While {@link fr.traqueur.items.api.registries.EffectsRegistry} stores effect definitions
 * and {@link fr.traqueur.items.api.registries.HandlersRegistry} stores effect handlers,
 * ApplicatorsRegistry stores the <b>application method</b>. This separation allows:</p>
 * <ul>
 *   <li>Same effect with different application methods on different servers</li>
 *   <li>Effects without applicators (command-only or auto-applied)</li>
 *   <li>Dynamic recipe generation based on applicator configuration</li>
 * </ul>
 *
 * @see Applicator
 * @see Effect
 * @see fr.traqueur.items.api.registries.EffectsRegistry
 * @see fr.traqueur.items.api.managers.EffectsManager
 */
public interface ApplicatorsRegistry extends Registry<String, Applicator> {

    /**
     * Retrieves the applicator associated with a specific effect.
     *
     * <p>This is a convenience method that looks up the applicator using the effect's ID.
     * It's equivalent to calling {@code registry.get(effect.id()).orElse(null)}.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Effect effect = ...; // Get effect from registry
     * Applicator applicator = applicatorsRegistry.getByEffect(effect);
     *
     * if (applicator != null) {
     *     // This effect has an applicator configured
     *     applicator.generateRecipe(effect);
     * } else {
     *     // This effect has no applicator (command-only or auto-applied)
     *     getLogger().info("Effect " + effect.id() + " has no applicator");
     * }
     * }</pre>
     *
     * @param effect the effect to find an applicator for
     * @return the applicator associated with the effect, or {@code null} if none exists
     * @throws NullPointerException if effect is null
     */
    Applicator getByEffect(Effect effect);
}