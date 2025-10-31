package fr.traqueur.items.listeners;

import fr.traqueur.items.api.ItemsPlugin;
import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Applicator;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectApplicationResult;
import fr.traqueur.items.api.effects.EffectRepresentation;
import fr.traqueur.items.api.managers.EffectsManager;
import fr.traqueur.items.api.registries.ApplicatorsRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.recipes.api.domains.Ingredient;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

import java.util.List;

/**
 * Listener for applying effects via smithing table.
 * Template slot (0): Required template ingredient
 * Base slot (1): Item to apply effect to
 * Addition slot (2): Effect representation item
 */
public class SmithingTableListener implements Listener {

    private final ItemsPlugin plugin;

    public SmithingTableListener(ItemsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSmithing(PrepareSmithingEvent event) {
        SmithingInventory inventory = event.getInventory();
        Player player = (Player) event.getView().getPlayer();

        EffectsManager effectsManager = plugin.getManager(EffectsManager.class);
        if (effectsManager == null) {
            return;
        }

        // Get items from slots
        ItemStack template = inventory.getItem(0);  // Template slot
        ItemStack baseItem = inventory.getItem(1);  // Base item slot
        ItemStack additionItem = inventory.getItem(2);  // Addition slot (effect item)

        // Check if addition is an effect item
        if (additionItem == null || !effectsManager.isEffectItem(additionItem) || additionItem.getType().isAir()) {
            return; // Not an effect application
        }

        Effect effect = effectsManager.getEffectFromItem(additionItem);
        if (effect == null) {
            Logger.debug("Effect not found for effect item");
            return;
        }

        // Check if effect has smithing table representation
        EffectRepresentation representation = effect.representation();
        if (representation == null || representation.applicatorType() != EffectRepresentation.ApplicatorType.SMITHING_TABLE) {
            Logger.debug("Effect {} does not support smithing table application", effect.id());
            return;
        }

        // Validate template if required
        Ingredient templateIngredient = representation.getTemplateIngredient();
        if (templateIngredient != null) {
            if (template == null || !templateIngredient.isSimilar(template)) {
                Logger.debug("Invalid template for effect {}", effect.id());
                return;
            }
        }

        // Check if base item is valid
        if (baseItem == null || baseItem.getType().isAir()) {
            return;
        }

        // Get applicator to validate
        ApplicatorsRegistry applicatorsRegistry = Registry.get(ApplicatorsRegistry.class);
        if (applicatorsRegistry == null) {
            return;
        }

        Applicator applicator = applicatorsRegistry.getByEffect(effect);
        if (applicator == null) {
            Logger.debug("Applicator not found for effect {}", effect.id());
            return;
        }

        if (!applicator.canApply(baseItem, List.of(additionItem))) {
            Logger.debug("Cannot apply effect {} to item: validation failed", effect.id());
            return;
        }

        // Clone the base item and apply the effect
        ItemStack result = baseItem.clone();
        EffectApplicationResult applicationResult = effectsManager.applyEffect(player, result, effect);

        if (applicationResult == EffectApplicationResult.SUCCESS) {
            event.setResult(result);
            Logger.debug("Effect {} applied via smithing table", effect.id());
        } else {
            Logger.debug("Failed to apply effect {}: {}", effect.id(), applicationResult);
        }
    }
}