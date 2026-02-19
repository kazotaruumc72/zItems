package fr.traqueur.items.api.settings.models;

import fr.traqueur.recipes.api.Util;
import fr.traqueur.recipes.api.domains.Ingredient;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.api.Loadable;

/**
 * Settings for an ingredient that can be parsed into a RecipeAPI Ingredient.
 * Supports various formats:
 * - "DIAMOND" - Simple material
 * - "material:STONE" - Explicit material
 * - "tag:planks" - Minecraft tag
 * - "base64:xyz..." - Serialized ItemStack
 * - "zitems:custom_item_id" - Custom item via hook
 * @param item The item representation
 * @param sign Optional sign for comparison
 * @param strict Whether to enforce strict matching
 */
public record IngredientWrapper(
        String item,
        @Options(optional = true) Character sign,
        @Options(optional = true) @DefaultBool(false) boolean strict
) implements Loadable {

    /**
     * Converts this settings object to a RecipeAPI Ingredient.
     *
     * @return The parsed ingredient
     */
    public Ingredient toIngredient() {
        return Util.parseIngredient(item, sign, strict);
    }
}