package fr.traqueur.items.api.effects;

import fr.traqueur.recipes.api.domains.Ingredient;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Represents a recipe for applying an effect to an item.
 * Validates that the correct items and ingredients are present.
 */
public record Applicator(
        Effect effect,
        List<Ingredient> ingredients,
        EffectRepresentation.ApplicatorType type
) {

    /**
     * Checks if this applicator can be used with the given items.
     *
     * @param baseItem The item to apply the effect to
     * @param inputItems The ingredients provided
     * @return true if the application is valid
     */
    public boolean canApply(ItemStack baseItem, List<ItemStack> inputItems) {
        if (baseItem == null || baseItem.getType().isAir()) {
            return false;
        }

        // Check if the effect can be applied to this material
        if (!effect.settings().canApplyTo(baseItem)) {
            return false;
        }

        // Check if effectItem is actually this effect's representation
        // This will be implemented in the manager to detect effect items

        // Validate ingredients if any
        if (ingredients != null && !ingredients.isEmpty()) {
            // Create a mutable copy of inputItems to track consumption
            List<ItemStack> remainingInputs = inputItems.stream()
                    .map(ItemStack::clone)
                    .toList();

            for (Ingredient ingredient : ingredients) {
                boolean found = false;

                // Try to find and consume this ingredient
                for (ItemStack input : remainingInputs) {
                    if (input == null || input.getType().isAir()) {
                        continue;
                    }

                    if (ingredient.isSimilar(input) && input.getAmount() >= 1) {
                        found = true;
                        input.setAmount(input.getAmount() - 1);
                        break;
                    }
                }

                if (!found) {
                    return false; // Missing required ingredient
                }
            }
        }

        return true;
    }
}